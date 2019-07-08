from socket import error


import vegvisir.protos.vegvisirprotocol_pb2 as vgp
import vegvisir.proto.vegvisirNetwork_pb2 as network
from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.emulator.socket_opcodes import (MessageTypes as msgtype,
                                             ProtocolStatus as ps,
                                             CommunicationStatus as comstatus)
from vegvisir.network.abstract_network import Network


from google.protobuf.internal.decoder import _DecodeVarint32


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


MESSAGE_SIZE = 3
BYTE_LIMIT = 1024


# @brief: A class that handles emulation network operations.
class EmulationNetworkOperator(Network):
    
    """
       :param server: A VegvisirServer object.
       :param client: A VegvisirClient object.
    """
    def __init__(self, server, client):
        Network.__init__(self, userid=server.userid)
        self.server = server
        self.client = client
        self.buffer = bytearray()


    def receive(self, conn_socket):
        """
           Receive and deserialize a payload from the wire.
           :param conn_socket: A socket object.
        """
        payload = network.VegvisirProtocolMessage()
        message_len, start = self.read_from_buffer(conn_socket)
        if start == None:
             return message_len
        payload.ParseFromString(self.buffer[start:start + message_len])
        
        print("SUCCESSFUL PARSING ON %s\n" % self.userid)
        self.buffer = bytearray()
        print("MESSAGE SIZE %s\n" % message_len)
        return payload


    def read_from_buffer(self, conn_socket):
        """
            Read from the server buffer until end of message.
            :param conn_socket: A socket.
        """
        print("READING FROM BUFFER\n")
        status = self.check_incoming_bytes(conn_socket, MESSAGE_SIZE)
        if status != comstatus.SUCCESS: 
            return status, None

        start = 0
        message_len, start_pos = _DecodeVarint32(self.buffer, start)
        message_chunk = len(self.buffer) - start_pos
        if message_chunk < message_len:
            missing_bytes = message_len - message_chunk
            print("Receiving %s missing bytes\n" % missing_bytes)
            self.buffer = bytearray(self.buffer)
            if missing_bytes < BYTE_LIMIT:
                status = self.check_incoming_bytes(conn_socket, missing_bytes)
                if status != comstatus.SUCCESS: 
                    return status, None 
            else:
                factor = int(missing_bytes/BYTE_LIMIT)
                remainder = missing_bytes % BYTE_LIMIT
                for _ in range(factor):
                    status = self.check_incoming_bytes(conn_socket, BYTE_LIMIT)
                    if status != comstatus.SUCCESS: 
                        return status, None 
                # Receive the remainder.
                if remainder > 0:
                    status = self.check_incoming_bytes(conn_socket, remainder)
                    if status != comstatus.SUCCESS: 
                        return status, None 
        self.buffer = bytes(self.buffer)
        return message_len, start_pos


    def check_incoming_bytes(self, conn_socket, size):
         """
            :param conn_socket: A socket.
            :param size: An integer.
         """
         try:
             incoming_bytes = conn_socket.recv(size)
             if len(incoming_bytes) == 0:
                  print("No data received....\n")  
                  return comstatus.NO_DATA
         except error:
             print("Receiving failed....\n")  
             return comstatus.SOCKET_ERROR
         self.buffer += incoming_bytes
         return comstatus.SUCCESS 


    def send(self, payload, sender):
        """
           :param payload: A serialized bytestring.
           :param sender: A Socket object.
        """
        try:
            bytes_sent = sender.send(payload)
            print("%s bytes sent...\n" % bytes_sent)
            return comstatus.SUCCESS
        except error as socket_error:
            print("Sending failed with error %s...\n" %
                   error.__str__())
            return comstatus.SOCKET_ERROR
