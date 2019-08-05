from time import time, sleep
from random import randint, uniform

import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.protocols.frontier.frontierserver import FrontierServer
from vegvisir.protocols.sendall.sendallserver import SendallServer
from vegvisir.emulator.socket_opcodes import (MessageTypes as msgtype,
                                              ProtocolStatus as ps)


# @brief: A class that handles the vegvisir protocol server side.
class ServerController(object):

    """
        A data structure representing the state of a device.

        :param sendall_server: A SendallServer object.
        :param frontier_server: A FrontierServer object.
        :param request_handler: A RequestHandler for dispatching requests.
        :param vector_server: A VectorServer object.
    """
    def __init__(self, sendall_server, frontier_server,
                request_handler, vector_server):
        self.sendall_server = sendall_server
        self.frontier_server = frontier_server
        self.request_handler = request_handler
        self.userid = self.request_handler.userid
        self.network =  request_handler.network
        self.vector_server = vector_server


    def process_incoming_payload(self, peer_conn, inputs):
        """
            Receive, deserialize, and dispatch a payload from the wire.
            :param peer_conn: A socket object.
            :param inputs: A list of sockets.
        """
        message, message_type = self.network.receive(sender=peer_conn)
        if type(message_type) == None:
            exit(1)

        if message == None:
            print("%s processing first message failed!\n" % self.userid)
            print("%s Maybe the peer left!\n" % self.userid)
            return ps.CONNECTION_FAILURE 

        # Channel request/response based on highest common protocol
        if message_type == msgtype.REQUEST: 
            if message.type == vgp.PeerRequest.READY_FOR_PROTOCOL:
                print("Server %s rcvd rqst 2 start protocol\n" % self.userid)
                response_status, choice = self.request_handler.handle_protocol_list_request(message, peer_conn)
                if choice == vgp.FRONTIER:
                    protocol_status = self.frontier_server.run_fset_protocol(
                                                                     peer_conn)
                    print("\n\n INPUTS AT THE END %s \n\n" % inputs)
                    return protocol_status
                if choice == vgp.SEND_ALL:
                    protocol_status = self.sendall_server.receive_allblocks(
                                                                     peer_conn)
                    return protocol_status
                if choice == vgp.VECTOR:
                    print("Protocol choice is vector!\n")
                    return ps.ONGOING_VECTOR_PROTOCOL
                if choice == vgp.VERSION:
                    print("%s Speaks a different protocol than the client\n" %
                          self.userid)
                    return ps.PROTOCOL_DISAGREEMENT
            elif message.type == vgp.PeerRequest.ADD_BLOCK:
                print("SERVER %s HANDLING ADD_BLOCK REQUEST\n" % self.userid)
                self.request_handler.handle_add_block_request(message)
                return ps.ONGOING_VECTOR_PROTOCOL
            if message.type == vgp.PeerRequest.END_PROTOCOL:
                print("Server %s rcvd rqst 2 end vector protocol\n" %
                      self.userid)    

                # Check if we were receiving anything from client socket.
                client = self.network.client
                if peer_conn in self.network.inputs:
                    self.network.inputs.remove(peer_conn)
                    if peer_conn is client.client_socket:
                        print("CLIENT SOCKET provided INPUT\n")
                    else:
                        print("Prior client socket sent an END message\n")
                return ps.SUCCESS
        elif message_type == msgtype.UPDATE:
            update_type = message.WhichOneof("peer_updates")
            if update_type == "current_view":
                vector_clocks = vgp.VectorClock()
                vector_clocks.CopyFrom(message.current_view)
                print("Peer vector clock: %s\n" % vector_clocks)
                self.vector_server.handle_peer_update(vector_clocks, peer_conn)
                return ps.SUCCESS  
            elif update_type == "latest_happenings":
                # We might not need this protocol, but just in case.
                print("Implement me!\n")
                exit(1)
