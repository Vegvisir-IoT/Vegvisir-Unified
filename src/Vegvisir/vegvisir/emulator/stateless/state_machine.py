from time import time, sleep


from vegvisir.emulator.socket_opcodes import ProtocolState as state
from vegvisir.proto import handshake_pb2 as hs
from vegvisir.proto import sendall_pb2 as sa

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles the transition of states for each connection.
class StateMachine(object):

    """
       A data structure representing the state of a device with regards
       to reconciliation with nearby peers.
       :param network: A EmulationNetworkOperator object.
       :param frontier_handler: a FrontierHandler object.
       :param vector_server: a VectorServer object.
       :param handshake_handler: A HandshakeHandler object.
       :param sendall_server: A SendallServer object.
    """
     
    def __init__(self, network, frontier_handler, frontier_server,
                 sendall_server, vector_server, handshake_handler):
        self.network = network
        self.frontier_handler = frontier_handler
        self.frontier_server = frontier_server
        self.sendall_server = sendall_server
        self.userid = network.userid 
        self.vector_server = vector_server 
        self.handshake_handler = handshake_handler
        # For keeping track of states for each connection.
        self.states = {}


    def process_message(self, message, connection):
        """
           Process a message received from a connection before
           transitioning to the next state.
           :param message: oneof handshake, sendall, vector,
                           or frontier message.
           :param connection: A connection object.
        """
        if not connection in self.states:
            message_queue = self.network.message_queues[connection]
            self.states[connection] = {'message_queue': message_queue}
        state = self.states[connection]

        # Channel the message to the right handler.
        message_type = message.WhichOneof("message_types")
        if message_type == "handshake":
           if message.endProtocol:
               self.destroy_session(connection)
           else:
               payload = hs.HandshakeMessage()
               payload.CopyFrom(message.handshake)
               state = self.handshake_handler.handle_message(payload,
                                                       state['message_queue'])
               if state == state.PROTOCOL_DISAGREEMENT:
                   self.destroy_session(connection)
               else:
                   self.states[connection]['state'] = state
        elif message_type == "frontier":
           state = self.frontier_handler.handle_message(message,
                                                       self.states[connection]) 
           self.states[connection] = state
           if state['client_socket']: # Outgoing connection
               if state == state.REMOTE_DOMINATES:
                   nstate = self.frontier_handler.request_next_missing_block()
                   if nstate == state.EVEN:
                      nstate = self.frontier_handler.provide_pow(
                                                             end_protocol=True)
                      self.destroy_session(connection)
                   else:
                       self.states[connection]['state'] = state
           else: # Incoming connection 
               if state == state.REMOTE_DOMINATES:
                   nstate = self.frontier_handler.request_next_missing_block()
                   if nstate == state.EVEN and state['remote_is_subset']:
                       nstate = self.frontier_handler.provide_pow()
                       self.frontier_server.send_fset_request() 
                   elif nstate == state.EVEN: # Only server is behind.
                       nstate = self.frontier_handler.provide_pow(
                                                           end_protocol=True)
                       self.destroy_session(connection)
                   else: # More missing blocks exist
                       self.states[connection]['state'] = state
        #elif message_type == "vector":
        #    continue
        elif message_type == "sendall":
            sa_message = sa.SendallMessage()
            sa_message.CopyFrom(message.sendall)
            state = self.sendall_server.process_all_blocks(sa_message) 
            self.states[connection]['state'] = state
        if message.endProtocol and connection in self.states: 
            self.destroy_session(connection)


    def destroy_session(self, connection):
        """
           Remove the connection when devices have reached the same state.
           :param connection: A socket object.
        """
        if connection in self.states:
            del self.states[connection]
        self.network.remove_connection(connection)
