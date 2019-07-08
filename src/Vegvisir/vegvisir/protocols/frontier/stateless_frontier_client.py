from time import time


from vegvisir.protocols.protocol import Protocol
from vegvisir.emulator.socket_opcodes import ProtocolState as rstate
from vegvisir.proto import handshake_pb2 as hs


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class for handling frontier messages coming to the client side.
class FrontierClient(Protocol):

    """
       :param request_handler: A PeerRequestHandler object.
       :param request_creator: A ProtocolRequestCreator object.
       :param crash_prob: A float.
    """
    def __init__(self, request_creator, request_handler, crash_prob):
        Protocol.__init__(self, request_handler.userid,
                          request_creator.blockchain, crash_prob)
        self.request_creator = request_creator
        self.request_handler = request_handler


    def handle_fset_request(self, message, state):
        """
           Handle a peer's request for the frontier set.
           Additionally, notify the server whether it is behind.
           :param message: A HashSet message.
           :param state: A dictionary.
        """
        other_frontier_set = []
        for block_hash in message.hashes:
            other_frontier_set.append(block_hash)

        our_hashes = set(self.blockchain.blocks.keys())
        remote_server_is_subset = set(other_frontier_set).issubset(our_hashes)
        our_frontier_set = self.blockchain.crdt.frontier_set()
        response = self.request_handler.handle_fset_request(
                                                      remote_server_is_subset)
        state['message_queue'].put(response)
        if state['state'] == rstate.HANDSHAKE:
            state['protocol'] = hs.FRONTIER
            state['cached_blocks'] = []
            state['start_time'] = time(),
            state['other_fset'] = other_frontier_set,
            state['client_socket'] = True

        if remote_server_is_subset:
            return rstate.LOCAL_DOMINATES
        return rstate.RECONCILIATION


    def handle_reconciliation_request(self, outgoing_conn, message):
        """
           Handle the server's request to reconcile our view.
           :param outgoing_conn: A connection object.
           :param message: A Request message.
        """
        other_frontier_set = []
        for block_hash in message.hashes:
            other_frontier_set.append(block_hash)
        local_fset = self.blockchain.crdt.frontier_set()

        frontier_difference = list(other_frontier_set.difference(local_fset))
        state['missing_blocks'] =  frontier_difference
        return rstate.RECONCILIATION 


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
