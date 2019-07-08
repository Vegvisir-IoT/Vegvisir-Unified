from vegvisir.proto import handshake_pb2 as hs
from vegvisir.emulator.socket_opcodes import ProtocolState as state

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles any Handshake messages.
class HandshakeHandler(object):

    """
       :param request_handler: A PeerRequestHandler object.
       :param request_creator: A ProtocolRequestHandler object.
       :param frontier_server: A FrontierServer object.
    """
    def __init__(self, request_handler, request_creator, frontier_server):
        self.request_handler = request_handler
        self.request_creator = request_creator
        self.frontier_server = frontier_server


    def handle_message(self, message, message_queue):
        """
           :param message: A VegvisirProtocolMessage protobuf object.
           :param message_queue: A Queue object.
        """
        choice, rtype = self.request_handler.handle_protocol_list_request(
                                                                       message)
        
        if choice == hs.SEND_ALL:
            print("Peer at speaks the sendall protocol!\n")
            sendall_request = self.request_creator.add_all_blocks_request()
            message_queue.put(sendall_request) 
            return state.RECONCILIATION
        elif choice == hs.FRONTIER:
            print("Peer at %s speaks the frontier protocol!\n")
            fset_request = self.frontier_server.send_set_request(
                                                                first_run=True)
            message_queue.put(fset_request)
            return state.HANDSHAKE 
        elif choice == hs.VECTOR:
            print("Peer at %s speaks the vector protocol!\n")
            if rtype == hs.REQUEST:
                response = self.request_creator.handshake_request(hsRESPONSE,
                                        protocol=self.request_handler.protocol) 
                message_queue.put(response)
            else:
                update = self.request_creator.create_update(self.vector_clock)
                message_queue.put(update)
               
            return state.RECONCILIATION
        else: # No agreement on a protocol
            handshake_message = self.request_creator.handshake_request(
                                                             end_protocol=True)
            message_queue.put(handshake_message)
            return state.PROTOCOL_DISAGREEMENT
