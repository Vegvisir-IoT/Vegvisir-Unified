from time import time, sleep


from vegvisir.emulator.socket_opcodes import ProtocolState as rstate
from vegvisir.proto import (handshake_pb2 as hs,
                           sendall_pb2 as sa,
                           frontier_pb2 as frontier,
                           vector_pb2 as vp)


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
        print("Current states %s\n" % self.states)
        if not connection in self.states:
            message_queue = self.network.message_queues[connection]
            self.states[connection] = {'message_queue': message_queue}
        state = self.states[connection]
        print("Starting state %s\n" % state)

        # Channel the message to the right handler.
        message_type = message.WhichOneof("message_types")
        if message_type == "handshake":
           if message.endProtocol:
               self.destroy_session(connection)
           else:
               payload = hs.HandshakeMessage()
               payload.CopyFrom(message.handshake)
               state = self.handshake_handler.handle_message(payload, state)
               if state == rstate.PROTOCOL_DISAGREEMENT:
                   print("No agreement on protocol spoken\n")
               else:
                   self.states[connection]['state'] = state
        elif message_type == "frontier":
           frontier_message = frontier.FrontierMessage()
           frontier_message.CopyFrom(message.frontier)
           nstate = self.frontier_handler.handle_message(frontier_message,
                                                       state) 
           #self.states[connection]['state'] = nstate
           if state['client_socket']: # Outgoing connection
               print("processed OUTBOUND conn's message\n")
               if nstate == rstate.REMOTE_DOMINATES:
                   nstate = self.frontier_handler.request_next_missing_block(
                                                                         state)
                   if nstate == rstate.EVEN:
                      nstate = self.frontier_handler.provide_pow(
                                                             end_protocol=True)
                      self.destroy_session(connection)
                   else:
                       self.states[connection]['state'] = nstate
               #elif nstate ==
               else: # Client has to reconcile.
                   self.states[connection]['state'] = nstate
               print("New state is %s\n" % nstate)
           else: # Incoming connection 
               print("processed INBOUND conn's message\n")
               if nstate == rstate.REMOTE_DOMINATES:
                   nstate = self.frontier_handler.request_next_missing_block(
                                                                         state)
                   if nstate == rstate.EVEN and state['remote_is_subset']:
                       nstate = self.frontier_handler.provide_pow()
                       self.frontier_server.send_fset_request() 
                   elif nstate == rstate.EVEN: # Only server is behind.
                       nstate = self.frontier_handler.provide_pow(
                                                           end_protocol=True)
                       self.destroy_session(connection)
                   else: # More missing blocks exist
                       self.states[connection]['state'] = nstate
               print("New state is %s\n" % nstate)
        elif message_type == "vector":
           v_message = vp.VectorMessage()
           v_message.CopyFrom(message.vector) 
           vector_message_type = v_message.WhichOneof("vector_message_type")
           if vector_message_type == "worldView":
                nstate = self.vector_server.handle_peer_update(v_message,
                                                               state)
                if nstate == rstate.LOCAL_DOMINATES:
                    print("Peer is behind...\n")
                elif nstate == rstate.PROTOCOL_DISAGREEMENT:
                    print("Peer sent invalid vector clock\n")
                    message.endProtocol = True
                else:
                    print("Peer is not missing anything from local chain..\n")
                self.states[connection]['state'] = nstate
           else: # add blocks
               nstate = self.vector_server.process_blocks(v_message)
               self.states[connection]['state'] = nstate
        elif message_type == "sendall":
            sa_message = sa.SendallMessage()
            sa_message.CopyFrom(message.sendall)
            nstate = self.sendall_server.process_all_blocks(sa_message) 
            self.states[connection]['state'] = nstate
        if message.endProtocol and connection in self.states: 
            self.destroy_session(connection)


    def destroy_session(self, connection):
        """
           Remove the connection when devices have reached the same state.
           :param connection: A socket object.
        """
        if connection in self.states:
            del self.states[connection]
        if connection in self.network.outputs:
            self.network.outputs.remove(connection)
        self.network.remove_connection(connection)


    def update_state(self, connection, new_state):
        """
           Update the state of a connection.
           :param connection: A socket object.
           :param new_state: A Handshake enum.
        """
        if connection in self.states:
            self.states[connection]['state'] = new_state
        else:
            self.states[connection] = {'state': new_state}
            message_queue = self.network.message_queues[connection]
            self.states[connection]['message_queue'] = message_queue
