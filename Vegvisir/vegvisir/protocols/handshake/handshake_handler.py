
__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles any Handshake messages.
class HandshakeHandler(object):

    """
       :param request_handler: A PeerRequestHandler object.
       :param request_creator: A ProtocolRequestHandler object.
    """
    def __init__(self, request_handler, request_creator):
        self.request_handler = request_handler
        self.request_creator = request_creator

    def handle_message(self, message, message_queue):
        """
           :param message: A HandshakeMessage protobuf object.
           :param message_queue: A Queue object.
        """
        choice = self.request_handler.handle_protocol_list_request(payload)
        
        if protocol_choice == handshake.SEND_ALL:
            print("Peer at %s speaks the sendall protocol!\n" %
                                                      connection.getpeername())
            sendall_request = self.request_creator.add_all_blocks_request()
            message_queue.put(sendall_request) 
            return state.RECONCILIATION
        elif protocol_choice == handshake.FRONTIER:
            print("Peer at %s speaks the frontier protocol!\n" %
                                                      connection.getpeername())
            fset_request = self.request_creator.frontier_set_request()
            message_queue.put(fset_request)
            return state.HANDSHAKE 
        elif protocol_choice == handshake.VECTOR:
            print("Peer at %s speaks the vector protocol!\n" %
                                                      connection.getpeername())
            update = self.request_creator.create_update(self.vector_clock)
            message_queue.put(update)
            return state.RECONCILIATION
        else: # No agreement on a protocol
            handshake_message = self.request_creator.handshake_request(
                                                             end_protocol=True)
            message_queue.put(handshake_message)
            return state.PROTOCOL_DISAGREEMENT
