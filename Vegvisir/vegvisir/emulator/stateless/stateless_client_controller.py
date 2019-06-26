from random import randint, uniform
from time import time, sleep

import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.emulator.socket_opcodes import (ProtocolStatus as ps,
                                              CommunicationStatus as comstatus)

# @brief: A class that handles the vegvisir protocol client side.
class ClientController(object):

    """
        A data structure representing the state of a device.

        :param sendall_client: A SendallClient object.
        :param frontier_client: A FrontierClient object.
        :param request_handler: A PeerRequestHandler object. 
        :param request_creator: A ProtocolRequestCreator for creating requests.
    """
    def __init__(self, sendall_client, frontier_client, request_handler,
                 request_creator):
        self.sendall_client = sendall_client
        self.frontier_client = frontier_client
        self.request_handler = request_handler
        self.request_creator = request_creator
        self.network = request_creator.network
        self.userid = request_handler.userid 
        self.vector_clock = request_handler.vector_clock
        self.protocol = request_handler.protocol
        self.reconciliations = [] 


    def initiate_any_protocol(self, message_queue):
        """
            Initiates the Vegvisir protocol after connecting to a peer.
            The initial message sent should have a list of protocols that 
            the client can speak. The server replies with a regular message or
            a request to end the protocol when it does not agree with the client
            on a protocol.
            :param message_queue: A Queue object.
        """
        request = self.request_creator.initiate_protocol_request(
                                                            self.protocol)
        message_queue.put(request)
        return state.HANDSHAKE
