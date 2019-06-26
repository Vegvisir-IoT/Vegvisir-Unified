from random import randint, uniform
from time import time, sleep

import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.emulator.socket_opcodes import (ProtocolStatus as ps,
                                              CommunicationStatus as comstatus)
from vegvisir.protocols.protocol import Protocol


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

 
# @brief: A class that handles the vegvisir protocol using frontier sets..
class FrontierClient(Protocol):

    """
        A data structure representing the state of a device.

        :param private_key: An _EllipticCurvePrivateKey for signing blocks.
        :param client: A VegvisirClient for responding to peer requests.
        :param request_handler: A RequestHandler for dispatching requests.
        :param request_creator: A ProtocolRequestCreator for creating requests.
    """
    def __init__(self, private_key, request_handler, request_creator,
                 crash_prob):
        Protocol.__init__(self, request_handler.userid,
                          request_creator.blockchain, crash_prob)
        self.private_key = private_key
        self.request_handler = request_handler
        self.request_creator = request_creator
        self.network = request_handler.network
        self.client = self.network.client
        self.cache = {}
        self.reconciliations = [] 


    def receive_peer_request(self):
        """
            Receive a requested expected from peer.
        """
        request, _ = self.network.receive()
        if request == None:
            return None, None
        if request.type == vgp.PeerRequest.SEND_FRONTIER_SET:
            print("CLIENT %s HANDLING FSET REQUEST\n" % self.userid)
            status = self.request_handler.handle_fset_request(server=False)
            return status, request 
        elif request.type == vgp.PeerRequest.SEND_BCHASHES:
            print("CLIENT %s HANDLING BC_HASHES REQUEST\n" % self.userid)
            status = self.request_handler.handle_bc_hashes_request(
                                                    self.client.client_socket,
                                                    server=False)
            return status, request 
        elif request.type == vgp.PeerRequest.SEND_BLOCK:
            print("CLIENT %s HANDLING SEND_BLOCK REQUEST\n" % self.userid)
            status = self.request_handler.handle_block_request(request,
                                                self.client.client_socket,
                                                server=False)
            return status, request
        elif request.type == vgp.PeerRequest.ADD_BLOCK:
            print("CLIENT %s HANDLING ADD_BLOCKS REQUEST\n" % self.userid)
            status = self.request_handler.handle_add_block_request(request)
            return status, request
        else:
            print("CLIENT HANDLING PROTOCOL CONTROL MESSAGE\n")
            return None, request
        
    def run_fset_protocol(self):
        """
            Run the vegvisir protocol when a peer makes a connection.
            :rtype: A boolean. Whether the protocol started/ended successfully.
        """
        while True:
            # Receive and handle a request for frontier set
            print("Client %s waiting for fset rqst" % self.userid)
            status, request = self.receive_peer_request()
            if request == None:
                print("%s CLIENT COULD NOT RECEIVE SERVER FSET REQUEST\n" 
                      % self.userid)
                return ps.REQUEST_DELAY_FSET 
            elif status == ps.RECONCILIATION_FAILURE:
                return status

            # Receive and handle request for blockchain hashes
            print("Client %s waiting for bc hashes rqst" % self.userid)
            status, request = self.receive_peer_request()
            if request == None:
                print("%s CLIENT COULD NOT RECEIVE SERVER BCHASHES REQUEST\n"
                      % self.userid)
                return ps.REQUEST_DELAY_BCHASHES 
            elif status == ps.RECONCILIATION_FAILURE:
                return status

            # Wait for message whether reconciliation needs to proceed
            print("Client %s ... fate of reconciliation" % self.userid)
            _, request = self.receive_peer_request()
            if request == None:
                print("%s CLIENT COULD NOT RECEIVE SERVER PROTOCOL DECISION\n"
                      % self.userid)
                return ps.REQUEST_DELAY_DECISION 

            if request.type == vgp.PeerRequest.SELF_RECONCILIATION_NEEDED:
                print("Client %s needs to reconcile\n" % self.userid)
                # Ask the peer for their frontier set.
                f_set_request = self.request_creator.frontier_set_request()

                # Send the request
                status = self.network.send(f_set_request, request_type="FRONTIER")
                if status == comstatus.SOCKET_ERROR:
                    return ps.RECONCILIATION_FAILURE
                print("Client %s sent fset request\n" % self.userid)

                # Receive the peer frontier set.
                other_fset, _ = self.network.receive()

                if other_fset == None:
                    print("RECONCILIATION FAILED HALFWAY AT %s CLIENT \
                       WHEN RECEIVING FRONTIER SET\n" %
                       self.userid)
                    return ps.FSET_REQUEST_FAILURE
                other_fset = set(other_fset)

                missing_blocks = []
                for missing_bhash in request.target_hashes:
                    missing_blocks.append(missing_bhash.hash)

                recon_status = self.reconcile(missing_blocks,
                                              are_we_behind=True)

                # Exit protocol gracefully if reconciliation fails.
                if recon_status != ps.SUCCESS:
                    return ps.RECONCILIATION_FAILURE

                # Check updated frontier set after reconciliation.
                frontier_set = self.blockchain.crdt.frontier_set()

                pow_parents = frontier_set.union(other_fset)
                pow_block = self.create_pow(pow_parents)

                # Add the POW block to our blockchain
                self.blockchain.add(pow_block, Operation.PROVIDED_POW)

                # Create a request for the peer to add our POW block.
                pow_request = self.request_creator.add_blocks_request(
                                                            blocks=[pow_block])

                # Send the request.
                status = self.network.send(pow_request,
                                           request_type="ADD BLOCK")
                if status == comstatus.SOCKET_ERROR:
                    return ps.RECONCILIATION_FAILURE


            elif request.type == vgp.PeerRequest.PEER_RECONCILIATION_NEEDED:
                missing_blocks = []
                for missing_bhash in request.target_hashes:
                    missing_blocks.append(missing_bhash.hash)
                recon_status = self.reconcile(missing_blocks, 
                                              are_we_behind=False)

                # Exit protocol gracefully if reconciliation fails
                if recon_status != ps.SUCCESS:
                    return ps.RECONCILIATION_FAILURE

                # Update the number of users that have seen certain blocks.

            elif request.type == vgp.PeerRequest.END_PROTOCOL:
                print("CLIENT %s RECEIVED 'END PROTOCOL' MESSAGE\n" % self.userid)
                break
        return ps.SUCCESS

    def reconcile(self, missing_blocks, are_we_behind=False):
        """
            Reconcile any missing blocks with peer.
            :param missing_blocks: A list of missing block hashes.
            :param are_we_behind: A boolean.
        """
        print("CLIENT %s ENTERING RECONCILIATION\n" % self.userid)
        cached_blocks = []
        total_blocks = 0
        if are_we_behind:
            print("Note: Client %s is behind\n" % self.userid)
            start_time = time()
            while (len(missing_blocks) != 0):
                # Emulate probability of crash in the middle of reconciliation.
                self.emulate_crash_probability()

                bhash = missing_blocks.pop(0)
                if bhash in self.blockchain.blocks.keys():
                    print("bhash is on the chain already %s\n" % bhash)
                    continue
                elif bhash in self.cache:
                    print("bhash is in cache %s\n" % bhash)
                    block = self.cache[bhash]
                    for x in block.parents:
                        missing_blocks.append(x)
                else:
                    # Request and add block to cache
                    print("b_hash is not in blockchain or cache %s\n" % bhash)
                    cached_blocks.append(bhash)
                    block_request = self.request_creator.block_request(bhash)
                    status = self.network.send(block_request,
                                               request_type="SEND BLOCK")
                    if status == comstatus.SOCKET_ERROR:
                        return ps.RECONCILIATION_FAILURE

                    # Receive the block from peer.
                    block_response, _ = self.network.receive(client=self.client,
                                             userid=self.userid)
                    if block_response == None:
                        print("RECONCILIATION FAILED HALFWAY AT %s CLIENT \
                              IN RECEIVING REGULAR BLOCK FROM SERVER\n" %
                               self.userid)
                        return ps.BLOCK_REQUEST_FAILURE
                    
                    # Add block to cache and search for its parents
                    self.cache[block_response.hash()] = block_response
                    for x in block_response.parents:
                        missing_blocks.append(x)

                    # Add to the number of blocks that have been reconciled
                    total_blocks += 1
            cached_blocks.reverse()
            self.blockchain.add_list(cached_blocks, self.cache)
            self.cache = {}

            # Update reconciliation times
            recon_data = {'total_blocks': total_blocks,
                            'recon_time': time() - start_time}
            print("----- %s CLIENT RECONCILIATION RESULTS -----\n\n" % self.userid)
            print("%s Client, Missing blocks %s, Total rec time %s secs" % (
                   self.userid, recon_data['total_blocks'],
                    recon_data['recon_time']))
            self.reconciliations.append(recon_data)
        else:
            print("Note: Client %s is ahead\n" % self.userid)
            print("Peer needs %s\n" % missing_blocks)
            # start_time = time()
            while(True):
                status, request = self.receive_peer_request()
                if request == None:
                    print("%s CLIENT COULD NOT RECEIVE SERVER MISSING BLOCK \
                           REQUEST\n" % self.userid)
                    return ps.REQUEST_DELAY_BLOCK
                elif status == ps.RECONCILIATION_FAILURE:
                    return status
                elif request.type == vgp.PeerRequest.SEND_BLOCK:
                    for fulfilled_hash in request.target_hashes:
                        if missing_blocks:
                            missing_blocks.remove(fulfilled_hash.hash)
                            print("Fulfilled missing block %s\n" % 
                                  fulfilled_hash.hash)
                        else:
                            print("Fulfilled recursive block %s\n" %
                                  fulfilled_hash.hash)
                elif request.type == vgp.PeerRequest.ADD_BLOCK:
                    break
            print("%s CLIENT EXITING RECONCILIATION\n" % self.userid)
        # TO-DO: Revisit the decision to return a POW block
        return ps.SUCCESS

    def create_pow(self, pow_hashes):
        """
            Creates a proof-of-witness block based on the block received
            from a peer.
            :param pow_hashes: A set of block hashes.
        """
        print("CLIENT %s CREATING POW BLOCK\n" % self.userid)
        parents = list(pow_hashes)
        print("PARENTS OF POW ARE %s" % parents) 
        tx_dict = {'recordid': randint(1, 60000), 'comment': b'Type: POW;'}
        new_tx = Transaction(self.userid, time(), tx_dict)
        block = Block(self.userid, time(), parents, [new_tx])
        block.sign(self.private_key)
        return block

    def print_reconciliation_stats(self):
        """ Print the simulated reconciliation times. """

        print("----- %s CLIENT RECONCILIATION RESULTS -----\n\n", self.userid)
        all_recs = len(self.reconciliations)
        total_rec = 0
        for rec in self.reconciliations:
            total_rec += rec['recon_time']                        
        if all_recs > 0:
            avg_rec_time = float(total_rec / all_recs)
        print("Average rec time %s\n" % avg_rec_time)

