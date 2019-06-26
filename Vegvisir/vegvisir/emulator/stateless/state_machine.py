from time import time, sleep

import vegvisir.protos.vegvisirprotocol_pb2 as vgp


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles the transition of states for each connection.
class StateMachine(object):

    """
       A data structure representing the state of a device with regards
       to the blockchain.
       :param network: A EmulationNetworkOperator object.
       :param frontier_handler: a FrontierHandler object.
       :param vector_server: a VectorServer object.
    """
     
    def __init__(self, network, frontier_handler, frontier_server,
                 vector_server):
        self.network = network
        self.frontier_handler = frontier_handler
        self.frontier_server = frontier_server
        self.userid = frontier_client.userid 
        self.vector_server = vector_server 
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
            self.states[connection] = {}
        state = self.states[connection]

        # Channel the message to the right handler.
        message_type = message.WhichOneof("message_types")
        if message_type == "handshake":
           if message.endProtocol:
               self.destroy_session(connection)
           else:
               payload = vegvisir.protocol.datatype.HandshakeMessage()
               payload.CopyFrom(message.handshake)
               state = self.handshake_handler.handle_message(payload)
               if state = state.PROTOCOL_DISAGREEMENT:
                   self.destroy_session(connection)
               else:
                   self.states[connection]['state'] = state
        elif message_type == "sendall":
            continue
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
        elif message_type == "vector":
            continue


    def destroy_session(self, connection):
        """
           Remove the connection when devices have reached the same state.
           :param connection: A socket object.
        """
        del self.states[connection]
        self.network.remove_connection(connection)
