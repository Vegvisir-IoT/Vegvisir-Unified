from random import randint, uniform
from time import time

import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.emulator.socket_opcodes import ProtocolStatus as ps
from vegvisir.protocols.protocol import Protocol


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

 
# @brief: A class that handles the vegvisir protocol by sending all blocks.
class SendallServer(Protocol):

    """
        A data structure representing a device.

        :param request_handler: A ProtocolRequestHandler for creating requests.
        :param crash_prob: An float.
    """
    def __init__(self, request_handler, crash_prob):
        Protocol.__init__(self, request_handler.userid,
                          request_handler.blockchain, crash_prob)
        self.userid = request_handler.userid
        self.request_handler = request_handler
        self.network = request_handler.network
        self.reconciliations = [] 

    def receive_peer_request(self, peer_conn):
        """
            Receive a peer request in a round of reconciliation.
            Dispatch to the appropriate handler based on the content.
            :param peer_conn: A socket object.
        """
        peer_request, _ = self.network.receive(sender=peer_conn)

        # Check that request was received successfully
        if peer_request == None:
            return None 

        # Check the request type.
        if peer_request.type == vgp.PeerRequest.ADD_BLOCK:
            print("SERVER %s HANDLING ADD_BLOCKS REQUEST\n" % self.userid)
            self.request_handler.handle_add_block_request(peer_request)
            return peer_request
        elif peer_request.type == vgp.PeerRequest.END_PROTOCOL:
            print("SERVER %s HANDLING END PROTOCOL REQUEST\n" % self.userid)
            return peer_request
        else:
            print("SERVER %s RECEIVED INCONSISTENT MESSAGE FOR SENDALL \
                   PROTOCOL" % self.userid)
            return None 

    def receive_allblocks(self, peer_conn):
        """
            Receive all blocks from remote peer.
            :param peer_conn: A socket object.
        """
        start_time = time()

        # Emulate probability of crash in the middle of reconcilation.
        self.emulate_crash_probability()

        # Check if peer sent a request to add all their blocks
        peer_request = self.receive_peer_request(peer_conn)
        if peer_request == None:
            # TO-DO: specify which error occured before closing socket.
            print("%s SERVER, RECEIPT OF ALL BLOCKS FAILED\n" % self.userid)
            return ps.RECONCILIATION_FAILURE 

        # Emulate probability of crash in the middle of reconcilation.
        self.emulate_crash_probability()

        # Attempt to receive 'end protocol' message
        peer_request = self.receive_peer_request(peer_conn)
        if peer_request == None:
            print("%s SERVER, RECEIPT OF 'END' MESSAGE FAILED\n" % self.userid)
            return ps.REQUEST_DELAY_DECISION

        end_time = time() - start_time
        recon_data = {'recon_time': end_time}

        print("----- %s SERVER SENDALL RECONCILIATION RESULTS -----\n\n" %

              self.userid)
        print("Total rec time %s secs" % recon_data['recon_time'])
        self.reconciliations.append(recon_data)


        return ps.SUCCESS

    def print_reconciliation_stats(self):
        """ Print the simulated reconciliation times. """

        print("----- %s SERVER RECONCILIATION RESULTS -----\n\n", self.userid)
        all_recs = len(self.reconciliations)
        total_rec = 0
        for rec in self.reconciliations:
            total_rec += rec['recon_time']                        
        if all_recs > 0:
            avg_rec_time = float(total_rec / all_recs)
        print("Average rec time %s\n" % avg_rec_time)

