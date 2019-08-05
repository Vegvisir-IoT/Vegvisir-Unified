from random import randint

from ..blockchain.block import Transaction, Block
from ..blockchain.blockdag import GenesisBlock
from .opcodes import Operation, Event, Request

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza, Kolbeinn Karlsson, Pablo Fiori"]


# @brief: A class that bridges model classes to the simulator
class Controller(object):

    """ A representation of a controller representing a device.

        :param userid: A string, the device's id.
        :param blockchain: A Blockchain.
        :param private_key: An _EllipticCurvePrivateKey, the user's private
                            for signing new blocks
        :param cab_sim: A Cab_Simulator, representing the movement of a cab.
        :param quorum: An integer, the number of witnesses needed for a block.
    """
    def __init__(self, userid, blockchain, private_key, cab_sim, quorum):
        self.userid = userid
        self.blockchain = blockchain
        self.private_key = private_key
        self.cab_simulator = cab_sim
        self.quorum = quorum
        self.quorum_mappings = {} # dict of when requests reach their quorum
        self.cache = {}
        self.r_set = {} # dict of requests that haven't met the quorum
        self.listening_port = {}  # time stamp to payload
        self.network = None
        self.file_descriptor = cab_sim.cab_handler.cab_struct[self.userid][1]
        # Location gets updated only when new requests are made.
        # POWs do not provide locations.
        # Any blocks from a reconciliation have locations that have been
        # communicated to the network node.
        self.location = None
        self.reconciliations = {}

    def process_event(self, event):
        """ Process an event from event queue.

            :param event: A tuple, representing the event.
        """
        timestamp_after_transit = event[0]
        event_data = event[1]
        sender = event_data[0]
        event_type = event_data[2]

        if event_type == Event.RECORD_REQUEST:
            self.record_request_event()
        elif event_type == Event.CONNECTION_BUILT:
            self.connection_established_event(sender)
        elif event_type == Event.PEER_REQUEST:
            self.process_request(sender, timestamp_after_transit,
                                 event_data[3:])
        else:  # PEER_SEND
            requested_data = event_data[3]
            request_timestamp = event_data[4]
            self.listening_port[request_timestamp] = requested_data

    def record_request_event(self):
        """ Processes a record request from the user(device). """
        block = self.cab_simulator.simulate_requests(self.userid,
                                                     self.file_descriptor, 10)
        tx_array = block['tx']
        if len(tx_array) == 0:
            print("Cab with user id %s no longer has any data to make"
                  "requests\n" % self.userid)
        else:
            location = block['loc']
            block = Block(self.userid, self.network.time,
                          list(self.blockchain.frontier_nodes), tx_array,
                          location)
            block.sign(self.private_key)
            self.blockchain.add(block, Operation.ADDED_REQUEST)
            self.r_set[block.hash()] = []
            self.location = location

    def process_request(self, peer, request_timestamp, request):
        """ Process a peer's request.

            :param peer: A Controller.
            :param request_timestamp: A float.
            :param request: A list, the peer's request.
        """
        request_type = request[0]
        if request_type == Request.SEND_FRONTIER_SET:
            self.network.send([Event.PEER_SEND,
                               self.blockchain.crdt.frontier_set(),
                               request_timestamp], sender=self,
                              destination=peer)
        elif request_type == Request.SEND_BCHASHES:
            self.network.send([Event.PEER_SEND, self.blockchain.blocks.keys(),
                               request_timestamp], sender=self,
                              destination=peer)
        elif request_type == Request.SEND_BLOCK:
            self.network.send([Event.PEER_SEND,
                               self.blockchain.blocks[request[1]],
                               request_timestamp], sender=self,
                              destination=peer)
        elif request_type == Request.ADD_BLOCK:
            self.blockchain.add(request[1], Operation.GOT_POW)
            # self.print_new_state(Operation.GOT_POW.name)

            # Update the number of users that have "witnessed" a given block.
            satisfied_blocks = []
            if b'Type: POW' in request[1].tx[0].comment:
                userid = request[1].userid
                for b_hash in self.r_set.keys():
                    if userid not in self.r_set[b_hash]:
                        self.r_set[b_hash].append(userid)
                    if len(self.r_set[b_hash]) >= self.quorum:
                        satisfied_blocks.append(b_hash)
                        block_creation_time = self.blockchain.blocks[b_hash].timestamp
                        quorum_met_time = self.network.time
                        self.quorum_mappings[b_hash] = [block_creation_time,
                                                        quorum_met_time]
            for b_hash in satisfied_blocks:
                self.r_set.pop(b_hash, None)

        elif request_type == Request.CREATE_POW:
            pow_block = self.create_pow(request[1], request[1])
            self.blockchain.add(pow_block, Operation.PROVIDED_POW)
            # self.print_new_state(Operation.PROVIDED_POW.name)
            self.network.send([Event.PEER_SEND, pow_block,
                               request_timestamp], sender=self,
                              destination=peer)
        elif request_type == Request.R_SET_SIZE:
            self.network.send([Event.PEER_SEND, len(self.r_set),
                               request_timestamp], sender=self,
                              destination=peer)

    def connection_established_event(self, peer):
        """
        Process the event of establishing a connection between two devices.
        Handles the reconciliation of blockchains based on the frontier sets.
        """
        while (True):
            # Request peer's frontier set.
            transit_time = self.network.send([Event.PEER_REQUEST,
                                              Request.SEND_FRONTIER_SET],
                                             sender=self, destination=peer)

            self.network.receive()
            other_fset = set(self.listen(transit_time))

            frontier_set = self.blockchain.crdt.frontier_set()

            # Check whether the peer's frontier set is a
            # subset of our blockchain.
            other_is_subset = other_fset.issubset(set(
                                                  self.blockchain.blocks.keys()
                                                  ))

            # Receive a list of hashes from peer's blockchain.
            transit_time = self.network.send([Event.PEER_REQUEST,
                                              Request.SEND_BCHASHES],
                                             sender=self, destination=peer)
            self.network.receive()
            other_bc_hashes = set(self.listen(transit_time))

            # Check whether our frontier set is a subset
            # of the peer's blockchain
            ours_is_subset = frontier_set.issubset(other_bc_hashes)

            if other_is_subset and ours_is_subset:
                break

            elif ours_is_subset:
                try:
                    missing_blocks = list(other_fset.difference(frontier_set))
                    self.reconcile(missing_blocks, peer)
                    frontier_set = self.blockchain.crdt.frontier_set()

                    transit_time = self.network.send([Event.PEER_REQUEST,
                                                      Request.R_SET_SIZE],
                                                     sender=self, destination=peer)
                    self.network.receive()
                    len_peer_r_set = self.listen(transit_time)

                    if len_peer_r_set > 0:
                        pow_block = self.create_pow(frontier_set, other_fset)
                        self.network.send([Event.PEER_REQUEST,
                                           Request.ADD_BLOCK, pow_block],
                                          sender=self, destination=peer)
                        self.blockchain.add(pow_block, Operation.PROVIDED_POW)
                        # self.print_new_state(Operation.PROVIDED_POW.name)
                except Exception as e:
                    print("Exception raised in providing POW block "
                          "-> %s\n" % e)

            elif other_is_subset:
                missing_blocks = list(frontier_set.difference(other_fset))
                peer.reconcile(missing_blocks, self)

                if len(self.r_set) > 0:
                    try:
                        transit_time = self.network.send([Event.PEER_REQUEST,
                                                          Request.CREATE_POW,
                                                          frontier_set],
                                                         sender=self,
                                                         destination=peer)
                        self.network.receive()
                        pow_block = self.listen(transit_time)

                        self.blockchain.add(pow_block, Operation.GOT_POW)
                        # self.print_new_state(Operation.GOT_POW.name)

                        # Update the number of users that have "witnessed"
                        # a given block.
                        userid = pow_block.userid
                        satisfied_blocks = []
                        for b_hash in self.r_set.keys():
                            if userid not in self.r_set[b_hash]:
                                self.r_set[b_hash].append(userid)
                            if len(self.r_set[b_hash]) >= 2:
                                satisfied_blocks.append(b_hash)
                                block_creation_time = self.blockchain.blocks[b_hash].timestamp
                                quorum_met_time = self.network.time
                                self.quorum_mappings[b_hash] = [block_creation_time,
                                                        quorum_met_time]
                        for b_hash in satisfied_blocks:
                            self.r_set.pop(b_hash, None)
                    except Exception as e:
                        print("Exception raised in handling POW block "
                              " -> %s\n" % e)

            else:  # Neither is a subset of the other
                missing_blocks = list(other_fset.difference(frontier_set))
                self.reconcile(missing_blocks, peer)
                # create a merge block that also serves as pow
                # i.e. the merge block will have multiple parents
                pow_block = self.create_pow(frontier_set, other_fset)
                self.blockchain.add(pow_block, Operation.PROVIDED_POW)
                # self.print_new_state(Operation.PROVIDED_POW.name)
        return "Connection building returned successfully?"

    def listen(self, time):
        """ Listen for a request update from peer. """
        data = self.listening_port[time]
        del self.listening_port[time]
        return data

    def create_pow(self, self_frontier, other_frontier):
        """ Creates a proof-of-witness block based on the block received from
            another user.
            :param self_frontier: A set of block hashes.
            :param other_frontier: A set of block hashes.
        """
        parents = list(self_frontier.union(other_frontier))
        tx_dict = {'recordid': randint(1, 60000), 'comment': b'Type: POW;'}
        new_tx = Transaction(self.userid, self.network.time, tx_dict)
        block = Block(self.userid, self.network.time, parents, [new_tx])
        block.sign(self.private_key)
        return block

    def reconcile(self, missing_blocks, peer):
        """ Reconcile any missing blocks with peer.
            :param missing_blocks: A list of missing block hashes.
            :param peer: A Controller.
        """
        recon_data = {'missing_blocks': len(missing_blocks)}
        cached_blocks = []
        try:
            start_time = self.network.time
            while (len(missing_blocks) != 0):
                bhash = missing_blocks.pop(0)
                if bhash in self.blockchain.blocks.keys():
                    continue
                elif bhash in self.cache:
                    block = self.cache[bhash]
                    for x in block.parents:
                        missing_blocks.append(x)
                else:
                    # Request and add block to cache
                    cached_blocks.append(bhash)

                    transit_time = self.network.send([Event.PEER_REQUEST,
                                                      Request.SEND_BLOCK,
                                                      bhash],
                                                     sender=self,
                                                     destination=peer)
                    self.network.receive()
                    request = self.listen(transit_time)

                    self.cache[request.hash()] = request

                    for x in request.parents:
                        missing_blocks.append(x)
            cached_blocks.reverse()
            self.blockchain.add_list(cached_blocks, self.cache)
            # self.print_new_state(Operation.RECONCILED_PEER.name)
            self.cache = {}

            recon_data['recon_time'] = self.network.time - start_time
            if peer not in self.reconciliations.keys():
                self.reconciliations[peer] = [recon_data]
            else:
                self.reconciliations[peer].append(recon_data)

        except Exception as e:
            print("Exception in reconciling blocks between peers -> %s \n" % e)

    def print_new_state(self, operation):
        """ Prints the new state of a dag based on the latest operation.

            :param operation: An Operation.
        """
        new_state = "\n\nUser " + str(self.userid) + " changed states\n"
        new_state += "New frontier -> "
        new_state += str(self.blockchain.crdt.frontier_set()) + "\n"
        new_state += "Latest operation -> " + operation + "\n\n"
        print(new_state)
