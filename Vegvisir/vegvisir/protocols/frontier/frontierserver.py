from time import time, sleep
from random import randint, uniform


import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.emulator.socket_opcodes import (ProtocolStatus as ps,
                                              CommunicationStatus as comstatus)
from vegvisir.protocols.protocol import Protocol


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles the vegvisir protocol using frontier sets.
class FrontierServer(Protocol):

    """
        A data structure representing the state of a device.

        :param private_key: An _EllipticCurvePrivateKey for signing blocks.
        :param request_handler: A RequestHandler for dispatching requests.
        :param request_creator: A ProtocolRequestCreator for creating requests.
        :param crash_prob: An integer.
    """
    def __init__(self, private_key, request_handler, request_creator,
                 crash_prob):
        Protocol.__init__(self, request_handler.network.userid,
                          request_creator.blockchain, crash_prob)
        self.private_key = private_key
        self.request_handler = request_handler
        self.request_creator = request_creator
        self.network = request_handler.network
        self.cache = {}
        self.reconciliations = [] 
    
    def receive_peer_request(self, peer_conn):
        """
            Receive a peer request in a round of reconciliation.
            Dispatch to the appropriate handler based on the content.
            :param peer_conn: A socket object.
        """
        # Ignore the message type because we know it's a request.
        peer_request, _ = self.network.receive(sender=peer_conn)

        # Check that request was received successfully.
        if peer_request == None:
            return None

        # Check the request type.
        if peer_request.type == vgp.PeerRequest.SEND_FRONTIER_SET:
            print("SERVER %s HANDLING FSET REQUEST\n" % self.userid)
            # Currently return the status of the request, no block is returned
            return self.request_handler.handle_fset_request(peer_conn)
        elif peer_request.type == vgp.PeerRequest.SEND_BCHASHES:
            print("SERVER %s HANDLING BC_HASHES REQUEST\n" % self.userid)
            self.request_handler.handle_blochain_hashes_request(peer_conn)
        elif peer_request.type == vgp.PeerRequest.SEND_BLOCK:
            print("SERVER %s HANDLING SEND_BLOCK REQUEST\n" % self.userid)
            self.request_handler.handle_block_request(peer_request, peer_conn)
        elif peer_request.type == vgp.PeerRequest.ADD_BLOCK:
            print("SERVER %s HANDLING ADD_BLOCK REQUEST\n" % self.userid)
            self.request_handler.handle_add_block_request(peer_request)
            return peer_request
        else:
            print("SERVER HANDLING PROTOCOL CONTROL MESSAGE\n")

        return peer_request

    def run_fset_protocol(self, peer_conn):
        """
            Run the Vegvisir protocol when a peer makes a connection.
            :param peer_conn: A socket for receiving from peer.
        """
        while True:
            # Send a request for frontier set
            frontier_request = self.request_creator.frontier_set_request()

            status = self.network.send(frontier_request,
                                       destination=peer_conn,
                                       request_type="FRONTIER")
            if status == comstatus.SOCKET_ERROR:
                return ps.RECONCILIATION_FAILURE
            print("%s Server sent a fset rqst" % self.userid)

            # Receive peer's frontier set
            frontier_list, _ = self.network.receive(sender=peer_conn)

            if frontier_list == None:
                print("RECONCILIATION FAILED HALFWAY AT %s SERVER \
                       WHEN RECEIVING FRONTIER SET\n" %
                       self.userid)
                return ps.FSET_REQUEST_FAILURE

            other_fset = set(frontier_list)
            print("OTHER FSET %s\n" % other_fset)

            frontier_set = self.blockchain.crdt.frontier_set()

            # Check whether the peer's frontier set is a 
            # subset of our blockchain.
            other_is_subset = other_fset.issubset(set(
                                                  self.blockchain.blocks.keys()
                                                  ))

            print("%s Server about to send bc hashes rqst\n" % self.userid)
            # Create a request for the peer's blockchain hashes
            bchashes_request = self.request_creator.bchashes_request()
            status = self.network.send(bchashes_request, destination=peer_conn,
                                       request_type="BLOCKCHAIN HASHES")
            if status == comstatus.SOCKET_ERROR:
                return ps.RECONCILIATION_FAILURE

            # Receive a list of hashes from peer's blokchain.
            other_bc_hashes, _ = self.network.receive(sender=peer_conn)

            if other_bc_hashes == None:
                print("RECONCILIATION FAILED HALFWAY AT %s SERVER \
                       WHEN RECEIVING CLIENT BC HASHES\n" %
                       self.userid)
                return ps.BCHASHES_REQUEST_FAILURE
            other_bc_hashes = set(other_bc_hashes) 

            # Check whether our frontier set is a subset
            # of the peer's blockchain
            ours_is_subset = frontier_set.issubset(other_bc_hashes)

            if other_is_subset and ours_is_subset:
                end_protocol = self.request_creator.end_protocol_request()
                status = self.network.send(end_protocol,
                                           destination=peer_conn,
                                           request_type="END PROTOCOL")
                if status == comstatus.SOCKET_ERROR:
                    return ps.RECONCILIATION_FAILURE
                print("%s SERVER SENT AN 'END PROTOCOL' REQUEST\n" %
                      self.userid)
                break
            
            elif ours_is_subset:
                print("%s Server needs to reconcile\n" % self.userid)
                try:
                    # Compute the missing blocks
                    missing_blocks = list(other_fset.difference(frontier_set))
                    
                    # Notify peer that we need to reconcile
                    request = self.request_creator.peer_reconciliation_request(missing_blocks)
                    status = self.network.send(request,
                                               destination=peer_conn,
                                               request_type="RECONCILIATION")
                    if status == comstatus.SOCKET_ERROR:
                        return ps.RECONCILIATION_FAILURE

                    recon_status = self.reconcile(missing_blocks, peer_conn,
                                                  are_we_behind=True)
 
                    # Exit protocol gracefully if reconciliation fails
                    if recon_status != ps.SUCCESS:
                        return ps.RECONCILIATION_FAILURE

                    # Check updated frontier set after reconciliation
                    frontier_set = self.blockchain.crdt.frontier_set()
                    print("UPDATED FRONTIER SET: %s\n" % frontier_set)

                    pow_parents = frontier_set.union(other_fset)
                    pow_block = self.create_pow(pow_parents)


                    # Add the POW block to our blockchain
                    self.blockchain.add(pow_block, Operation.PROVIDED_POW)
                    print("POW block ->")
                    pow_block.print_block()

                    # Create a request for the peer to add our POW block
                    pow_request = self.request_creator.add_blocks_request(
                                                           blocks=[pow_block])

                    status = self.network.send(pow_request,
                                               destination=peer_conn,
                                               request_type="ADD POW")
                    if status == comstatus.SOCKET_ERROR:
                        return ps.RECONCILIATION_FAILURE

                except Exception as e:
                    print("Exception raised in providing POW block "
                          "-> %s\n" % e)

            elif other_is_subset:
                print("Remote Client is subset of %s server\n" % self.userid)
                # Tell peer that they need to reconcile
                missing_blocks = list(frontier_set.difference(other_fset))
                request = self.request_creator.self_reconciliation_request(
                                                            missing_blocks)

                status = self.network.send(pow_request,
                                           destination=peer_conn,
                                           request_type="RECONCILIATION")
                if status == comstatus.SOCKET_ERROR:
                    return ps.RECONCILIATION_FAILURE

                # Receive peer's request for our frontier set and
                # dispatch the request appropriately.
                peer_request = self.receive_peer_request(peer_conn)
                
                if peer_request == None:
                    print("%s SERVER COULD NOT RECEIVE CLIENT FSET REQUEST\n" 
                          % self.userid)
                    return ps.REQUEST_DELAY_FSET

                # Enter reconciliation mode with peer
                recon_status = self.reconcile(missing_blocks, peer_conn)

                # Exit protocol gracefully if reconciliation fails
                if recon_status != ps.SUCCESS:
                    return ps.RECONCILIATION_FAILURE

                # Update the number of users that have seen certain blocks.
            
            else: # Neither is a subset of the other
                print("NEITHER IS SUBSET OF THE OTHER\n")
 
                # Notify peer that we need to reconcile
                missing_blocks = list(other_fset.difference(frontier_set))
                request = self.request_creator.peer_reconciliation_request(
                                                                missing_blocks)
                status = self.network.send(request,
                                           destination=peer_conn,
                                           request_type="RECONCILIATION")
                if status == comstatus.SOCKET_ERROR:
                    return ps.RECONCILIATION_FAILURE

                recon_status = self.reconcile(missing_blocks, peer_conn,
                                              are_we_behind=True)

                 # Exit protocol gracefully if reconciliation fails
                if recon_status != ps.SUCCESS:
                    return ps.RECONCILIATION_FAILURE

                # create a merge block that also serves as POW
                # i.e. the merge block will have multiple parents
                pow_block = self.create_pow(frontier_set.union(other_fset))

                # Add the POW block on the blockchain
                self.blockchain.add(pow_block, Operation.PROVIDED_POW)
                print("POW block ->")
                pow_block.print_block()

                # Create a request for the peer to add our POW block
                pow_request = self.request_creator.add_blocks_request(
                                                            blocks=[pow_block])

                status = self.network.send(pow_request,
                                           destination=peer_conn,
                                           request_type="ADD BLOCK")
                if status == comstatus.SOCKET_ERROR:
                    return ps.RECONCILIATION_FAILURE
        return ps.SUCCESS

    def reconcile(self, missing_blocks, peer_conn, are_we_behind=False):
        """ 
            Reconcile any missing blocks with peer.
            :param missing_blocks: A list of missing block hashes.
            :param peer_conn: A Socket for communicating with the peer.
            :param are_we_behind: A boolean.
        """
        print("SERVER %s ENTERING RECONCILIATION\n" % self.userid)
        cached_blocks = []
        total_blocks = 0
        if are_we_behind:
            print("Note: %s Server is behind\n" % self.userid)
            print("Missing blocks %s\n" % missing_blocks)
            start_time = time()
            while (len(missing_blocks) != 0):

                # Emulate probability of crash in the middle of reconciliation.
                self.emulate_crash_probability()

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
                    block_request = self.request_creator.block_request(bhash)
                    status = self.network.send(block_request,
                                               destination=peer_conn,
                                               request_type="SEND BLOCK")
                    if status == comstatus.SOCKET_ERROR:
                        return ps.RECONCILIATION_FAILURE

                    print("%s Server sent a block request\n" % self.userid)

                    # Receive the block from peer.
                    block_response, _ = self.network.receive(sender=peer_conn)
                    if block_response == None:
                        print("RECONCILIATION FAILED HALFWAY AT %s SERVER \
                              IN RECEIVING REGULAR BLOCK FROM CLIENT\n" %
                               self.userid)
                        return ps.BLOCK_REQUEST_FAILURE 
                    print("Hash of received block %s\n" % block_response.hash())
                    # Add block to cache and search for its parents
                    self.cache[block_response.hash()] = block_response
                    for x in block_response.parents:
                        missing_blocks.append(x)
                    # Add to the numbers of blocks that have been reconciled
                    total_blocks += 1
            cached_blocks.reverse()
            self.blockchain.add_list(cached_blocks, self.cache)
            print("%s Server adding from cache worked\n" % self.userid)
            self.cache = {}

            # Update reconciliation times
            recon_data = {'total_blocks': total_blocks,
                          'recon_time': time() - start_time}
            print("----- %s SERVER RECONCILIATION RESULTS -----\n\n" % self.userid)
            print("%s Server, Missing blocks %s, Total rec time %s secs" % (
                   self.userid, recon_data['total_blocks'],
                    recon_data['recon_time']))
            self.reconciliations.append(recon_data)
        else:
            print("Note: %s Server is ahead\n" % self.userid)
            print("Peer needs %s\n" % missing_blocks)
            start_time = time()
            while(True):
                peer_request = self.receive_peer_request(peer_conn)
                if peer_request == None:
                    print("%s SERVER COULD NOT RECEIVE CLIENT MISSING BLOCK \
                          REQUEST\n" % self.userid)
                    return ps.REQUEST_DELAY_BLOCK
                if peer_request.type == vgp.PeerRequest.SEND_BLOCK:
                    for fulfilled_hash in peer_request.target_hashes:
                        if missing_blocks:
                            missing_blocks.remove(fulfilled_hash.hash)
                            print("Fulfilled missing block %s\n" % 
                                  fulfilled_hash.hash)
                        else:
                            print("Fulfilled recursive block %s\n" %
                                  fulfilled_hash.hash)
                        # Add to the numbers of blocks that have been reconciled
                        total_blocks += 1    
                if peer_request.type == vgp.PeerRequest.ADD_BLOCK:
                    print("%s SERVER RECEIVED POW BLOCK\n" % self.userid)
                    break
            # Update reconciliation times
            recon_data = {'total_blocks': total_blocks,
                            'recon_time': time() - start_time}
            self.reconciliations.append(recon_data)
            print("%s SERVER EXITING RECONCILIATION\n" % self.userid)
            
        return ps.SUCCESS 
        
    def print_new_state(self, operation):
        """ Prints the new state of a dag based on the latest operation.

            :param operation: An Operation.
        """
        new_state = "\n\nUser " + str(self.userid) + " changed states\n"
        new_state += "New frontier -> "
        new_state += str(self.blockchain.crdt.frontier_set()) + "\n"
        new_state += "Latest operation -> " + operation + "\n\n"
        print(new_state)

    def create_pow(self, pow_hashes):
        """
            Create a proof-of-witness block based on the block received
            from a peer.
            :param pow_hashes: A set of block hashes.
        """
        print("SERVER %s CREATING POW BLOCK\n" % self.userid)
        parents = list(pow_hashes)
        print("PARENTS OF POW ARE %s\n" % parents)
        tx_dict = {'recordid': randint(1, 60000), 'comment': b'Type: POW;'}
        new_tx = Transaction(self.userid, time(), tx_dict)
        block = Block(self.userid, time(), parents, [new_tx])
        block.sign(self.private_key)
        return block

    def print_reconciliation_stats(self):
        """ Print the simulated reconciliation times. """

        print("----- %s SERVER RECONCILIATION RESULTS -----\n\n", self.userid)
        all_recs = len(self.reconciliations)
        for rec in self.reconciliations:
            total_peer_rec += rec['recon_time']
                                
        avg_rec_time = float(total_peer_rec / all_recs)
        print("Average rec time %s\n" % avg_rec_time)

