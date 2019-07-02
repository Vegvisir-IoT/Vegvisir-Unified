from time import time


from vegvisir.emulator.socket_opcodes import ProtocolState as state 
from vegvisir.protocols.protocol import Protocol


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
        # State will have a pointer when any function in here is called.
        self.state = None


    def send_fset_request(self, first_run):
        """
           Send the frontier set to a remote peer.
           Additionally, the request asks whether the server is behind the client.
           :param first_run: A boolean.
        """
        # Create a request for frontier set and whether the server is a subset.
        frontier_request = self.request_creator.frontier_set_request()
    
        # Add the request to the message queue for the connection.
        message_queue.put(frontier_set_request) 
    
        # Create the "state" for the connection.
        if first_run:
            self.state = {'protocol': vgp.FRONTIER,'cached_blocks': [],
                           'client_socket': False, 'start_time': time()}
            return state.HANDSHAKE
        else:
            return state.RECONCILIATION


    def handle_client_fset_update(self, other_frontier_set,
                                  server_is_subset=False):
        """
           Update the state for a connection based on a response from the client.
           :param other_frontier_set: A list of hashes.
           :param server_is_subset: A boolean.
        """
        our_hashes = set(self.blockchain.blocks.keys())
        remote_client_is_subset = set(other_frontier_set).issubset(our_hashes)

        # Update the "state" for the connection.
        if server_is_subset and remote_client_is_subset:
            end_protocol_request = self.request_creator.end_protocol_request()
            message_queue.put(end_protocol_request)
            return state.EVEN

        elif server_is_subset:
            self.state['server_is_subset'] = True

            # Create the list of missing blocks.
            local_fset = self.blockchain.crdt.frontier_set()
            frontier_difference = list(other_frontier_set.difference(local_fset))
            self.state['missing_blocks'] =  frontier_difference
            self.state['other_fset'] = other_frontier_set
            return state.REMOTE_DOMINATES

        else: # remote_client_is_subset:
            states[peer_conn]['remote_is_subset'] = True
    
            # Create a request for the peer to reconcile.
            reconciliation_request = self.request_creator.reconciliation_request()
            message_queue.put(reconciliation_request)
            return state.LOCAL_DOMINATES


    def handle_incoming_add_block_request(self, peer_conn, message):
        """
           Handle peer's request to add their proof-of-witness block.
           :param peer_conn: A connection object.
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
