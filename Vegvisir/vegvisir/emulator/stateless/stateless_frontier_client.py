from time import time


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class for handling frontier messages coming to the client side.
class FrontierClient(Protocol):

    """
       :param request_handler: A PeerRequestHandler object.
       :param request_creator: A ProtocolRequestCreator object.
    """
    def __init__(self, request_creator, request_handler):
        self.request_creator = request_creator
        self.request_handler = request_handler
        # State will have a pointer when any function in here is called.
        self.state = None


    def handle_fset_request(self, message):
        """
           Handle a peer's request for the frontier set.
           Additionally, notify the server whether it is behind.
           :param message: A HashSet message.
        """
        other_frontier_set = []
        for block_hash in message.hashes:
            other_frontier_set.append(block_hash)

        our_hashes = set(self.blockchain.blocks.keys())
        remote_server_is_subset = set(other_frontier_set).issubset(our_hashes)
        our_frontier_set = self.blockchain.crdt.frontier_set()
        response = self.request_handler.handle_fset_request(
                                                      remote_server_is_subset)
        message_queue.put(response)
        if self.state['state'] = state.HANDSHAKE:
            self.state['protocol'] = vgp.FRONTIER
            self.state['cached_blocks'] = []
            self.state['start_time'] = time(),
            self.state['other_fset'] = other_frontier_set,
            self.state['client_socket'] = True

        if remote_server_is_subset:
            return state.LOCAL_DOMINATES
        return state.RECONCILIATION


    def handle_reconciliation_request(self, outgoing_conn, message) 
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
        self.state['missing_blocks'] =  frontier_difference
        return state.RECONCILIATION 
