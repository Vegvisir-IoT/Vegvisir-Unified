from socket import (socket, error, errno, timeout, AF_INET,
                    SOCK_STREAM, SOL_SOCKET, SO_REUSEADDR, SHUT_RDWR)
from time import time


from google.protobuf.internal.decoder import _DecodeVarint32


from vegvisir.emulator.socket_opcodes import CommunicationStatus as comstatus


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

# Global constants
HOST = '127.0.0.1'
PORT = 8001
SOCKET_TIMEOUT = 2
MESSAGE_SIZE = 3
BYTE_LIMIT = 1024

# @brief: A class for connecting to a neighboring Vegvisir node.
class VegvisirClient(object):

    def __init__(self, userid):
        """
            Initialize a client instance on local host.
            :param userid: A string.
        """
        self.userid = userid
        self.client_socket = None
        self.connections = {}
        self.buffer = bytearray()

    def connect_to_peer(self, host, port):
        """
            Connect to a remote peer.
            :param host: A string of the peer's ip address.
            :param port: An int.
        """
        new_socket = None
        try:
            new_socket = socket(AF_INET, SOCK_STREAM)
            new_socket.settimeout(SOCKET_TIMEOUT)

            new_socket.connect((host, port))
            self.connections[new_socket] = [port, True]
            self.client_socket = new_socket
            return True
        except error as socket_error:
            self.client_socket = new_socket            
            self.connections[new_socket] = [port, False]
            return socket_error

    def shutdown_connection(self):
        """
           Shut down the connection no matter the protocol run return.
        """
        print("Client %s closing the connection...\n" % self.userid)
        if self.connections[self.client_socket][1] == True:
           self.client_socket.shutdown(SHUT_RDWR)
        del self.connections[self.client_socket]
        self.client_socket.close()


    def send(self, data_bytes):
        """
            Send a bytestring to remote peer.
            :param data_bytes: a serialized bytestring.
        """
        try:
            bytes_sent = self.client_socket.send(data_bytes)
            print("%s Client sent a %s byte message\n" % (self.userid,
                                        bytes_sent))
            return comstatus.SUCCESS, bytes_sent
        except error as socket_error:
            return comstatus.SOCKET_ERROR, socket_error

    def check_incoming_bytes(self, size):
         """
            Check whether the receive call resulted in an error.
            :param conn_socket: A socket.
            :param size: An integer.
            :rtype: A boolean.
         """
         incoming_bytes = self.receive_message(size)
         if type(incoming_bytes) != bytes:
              return comstatus.SOCKET_ERROR, incoming_bytes  
         else: 
             self.buffer += incoming_bytes
             return comstatus.SUCCESS, True 

    def read_from_buffer(self):
        """
            Read from the server buffer until end of message.
            :param conn_socket: A socket.
        """
        print("CLIENT READING FROM BUFFER\n")
        status, incoming_bytes = self.check_incoming_bytes(MESSAGE_SIZE)
        if status == comstatus.SOCKET_ERROR: 
            return incoming_bytes, None 
        else:
            start = 0
            message_len, start_pos = _DecodeVarint32(self.buffer, start)
            message_chunk = len(self.buffer) - start_pos
            if message_chunk < message_len:
                missing_bytes = message_len - message_chunk
                print("Client about to recv %s missing bytes\n" % missing_bytes)
                self.buffer = bytearray(self.buffer)
                if missing_bytes < BYTE_LIMIT:
                    status, incoming_bytes = self.check_incoming_bytes(
                                                                   
                                                                  missing_bytes)
                    if status == comstatus.SOCKET_ERROR:
                        return incoming_bytes, None
                else:
                    factor = int(missing_bytes/BYTE_LIMIT)
                    remainder = missing_bytes % BYTE_LIMIT
                    for _ in range(factor):
                        status, incoming_bytes = self.check_incoming_bytes(
                                                                   
                                                                  BYTE_LIMIT)
                        if status == comstatus.SOCKET_ERROR:
                            return incoming_bytes, None
                    # Receive the remainder.
                    if remainder > 0:
                        status, incoming_bytes = self.check_incoming_bytes(
                                                                   conn_socket,
                                                                  remainder)
                        if status == comstatus.SOCKET_ERROR:
                            return incoming_bytes, None
            self.buffer = bytes(self.buffer)
            return message_len, start_pos

    def receive_message(self, buffer_size):
        """
            Receive a message from remote peer.
            :param buffer_size: an integer specifying how much to receive.
        """
        try:
            data_bytes = self.client_socket.recv(buffer_size)
            # print("%s Client received %s bytes\n" % (self.userid,
             #                                   len(data_bytes)))
            if len(data_bytes) == 0:
                return None
            return data_bytes
        except error as socket_error:
            return socket_error  

