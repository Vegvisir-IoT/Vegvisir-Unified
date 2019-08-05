from time import time


from vegvisir.emulator.socket_opcodes import ProtocolState as rstate 
from vegvisir.protocols.protocol import Protocol
import vegvisir.proto.handshake_pb2 as hs


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

# @brief: A class for handling frontier messages coming to the server side.
class FrontierServer(Protocol):

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


    def send_fset_request(self, state, first_run):
        """
           Send the frontier set to a remote peer.
           Additionally, the request asks whether the server is behind the client.
           :param state: A dictionary.
           :param first_run: A boolean.
        """
        # Create a request for frontier set and whether the server is a subset.
        frontier_request = self.request_creator.frontier_set_request()
    
        # Add the request to the message queue for the connection.
        state['message_queue'].put(frontier_request) 
    
        # Create the "state" for the connection.
        if first_run:
            state['protocol'] = hs.FRONTIER
            state['cached_blocks'] = []
            state['cache'] = {}
            state['reconciled_blocks'] = 0
            state['client_socket'] = False,
            state['start_time'] = time()
            return rstate.HANDSHAKE
        else:

            # Emulate probability of crash in the middle of reconciliation.
            self.emulate_crash_probability()

            return rstate.RECONCILIATION


    def handle_client_fset_update(self, other_frontier_set, state,
                                  server_is_subset=False):
        """
           Update the state for a connection based on a response from the client.
           :param other_frontier_set: A list of hashes.
           :param server_is_subset: A boolean.
           :state: A dictionary.
        """
        our_hashes = set(self.blockchain.blocks.keys())
        remote_client_is_subset = set(other_frontier_set).issubset(our_hashes)

        # Update the "state" for the connection.
        if server_is_subset and remote_client_is_subset:
            end_protocol_request = self.request_creator.end_protocol_request()
            state['message_queue'].put(end_protocol_request)
            return rstate.EVEN

        elif server_is_subset:
            state['server_is_subset'] = True

            # Create the list of missing blocks.
            local_fset = self.blockchain.crdt.frontier_set()
            frontier_diff = list(set(other_frontier_set).difference(
                                                                   local_fset))
            state['missing_blocks'] =  frontier_diff
            state['other_fset'] = other_frontier_set
            return rstate.REMOTE_DOMINATES

        else: # remote_client_is_subset:
            state['remote_is_subset'] = True
    
            # Create a request for the peer to reconcile.
            local_fset = self.blockchain.crdt.frontier_set()
            frontier_diff = list(local_fset.difference(
                                                     set(other_frontier_set)))
            recon_request = self.request_creator.reconciliation_request(
                                                          frontier_diff)
            state['message_queue'].put(recon_request)
            return rstate.LOCAL_DOMINATES


    def handle_incoming_add_block_request(self, message):
        """
           Handle peer's request to add their proof-of-witness block.
           :param message: A VegvisirProtocolMessage.
        """
        self.request_handler.handle_add_block_request(message)


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
