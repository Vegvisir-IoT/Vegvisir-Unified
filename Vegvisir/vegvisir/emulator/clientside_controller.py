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


    def initiate_any_protocol(self):
        """
            Initiates the Vegvisir protocol after connecting to a peer.
            The initial message sent should have a list of protocols that 
            the client can speak. The server notifies the client which ones
            it understands.
        """
        request = self.request_creator.initiate_protocol_request(
                                                            self.protocol)
        status = self.network.send(request, request_type="INITIATE PROTOCOL")

        if status == comstatus.SOCKET_ERROR:
            return ps.RECONCILIATION_FAILURE
        print("%s sent PROTOCOL START message" % self.userid)

        # Wait for a response from the server
        protocol_choice, _ = self.network.receive() 
        if protocol_choice == None:
            print("%s Client could not received protocol list" % self.userid)
            return ps.REQUEST_DELAY_PROTOCOL_LIST
 
        elif protocol_choice == vgp.SEND_ALL:
            send_status = self.sendall_client.sendallblocks()
            return send_status

        elif protocol_choice == vgp.FRONTIER:
            status = self.frontier_client.run_fset_protocol()
            return status
        elif protocol_choice == vgp.VECTOR:
            print("Server speaks the vector protocol!\n")
            update = self.request_creator.create_update(self.vector_clock)
            status = self.network.send(update)

            if status == comstatus.SOCKET_ERROR:
                return ps.RECONCILIATION_FAILURE
            return ps.ONGOING_VECTOR_PROTOCOL
        else: # We don't agree on a protocol
            print("CHOICE %s\n" % protocol_choice)
            return ps.PROTOCOL_DISAGREEMENT
