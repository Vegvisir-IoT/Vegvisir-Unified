from vegvisir.proto import handshake_pb2 as hs
from vegvisir.emulator.socket_opcodes import ProtocolState as rstate

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


    def handle_message(self, message, state):
        """
           :param message: A VegvisirProtocolMessage protobuf object.
           :param state: A dictionary.
        """
        choice, rtype = self.request_handler.handle_protocol_list_request(
                                                                       message)
        
        if choice == hs.SEND_ALL:
            print("Peer speaks the sendall protocol!\n")
            sendall_request = self.request_creator.add_all_blocks_request()
            state['message_queue'].put(sendall_request) 
            return rstate.RECONCILIATION
        elif choice == hs.FRONTIER:
            print("Peer speaks the frontier protocol!\n")
            self.frontier_server.send_fset_request(state, first_run=True)
            return rstate.HANDSHAKE 
        elif choice == hs.VECTOR:
            print("Peer speaks the vector protocol!\n")
            if rtype == hs.HandshakeMessage.REQUEST:
                response = self.request_creator.handshake_request(
                                                hs.HandshakeMessage.RESPONSE,
                                        protocol=self.request_handler.protocol) 
                state['message_queue'].put(response)
            else:
                update = self.request_creator.create_update(self.vector_clock)
                state['message_queue'].put(update)
               
            return rstate.RECONCILIATION
        else: # No agreement on a protocol
            handshake_message = self.request_creator.handshake_request(
                                                  hs.HandshakeMessage.RESPONSE,
                                                             end_protocol=True)
            state['message_queue'].put(handshake_message)
            return rstate.PROTOCOL_DISAGREEMENT
