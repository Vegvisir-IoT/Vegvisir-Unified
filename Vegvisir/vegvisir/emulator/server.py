from socket import (socket, error, errno, timeout, AF_INET, SOCK_STREAM,
                    SOL_SOCKET, SO_REUSEADDR)
from time import time
from select import select


from google.protobuf.internal.encoder import _VarintBytes
from google.protobuf.internal.decoder import _DecodeVarint32

from vegvisir.emulator.socket_opcodes import (ProtocolStatus as ps,
                                              CommunicationStatus as comstatus)


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# Global constants
HOST = '127.0.0.1'
SOCKET_TIMEOUT = 4 
MESSAGE_SIZE = 3
BYTE_LIMIT = 1024


# @brief: A class for responding to a near by Vegvisir node.
class VegvisirServer(object):

    def __init__(self, port, userid):
        """
            Initialize a server instance on given port.
            :param port: An integer.
            :param userid: A string.
        """
        self.port = port
        self.userid = userid
        self.server_socket = socket(AF_INET, SOCK_STREAM)
        self.buffer = bytearray()


    def bind_server(self):
        """
            Bind the server to listen for connections.
        """
        try:
            self.server_socket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
            self.server_socket.bind((HOST, self.port))
            self.server_socket.listen(10)
            self.server_socket.settimeout(SOCKET_TIMEOUT)
        except Exception as e:
            print("Exception binding server -> %s\n" % e)
            exit(1)


    def check_incoming_bytes(self, conn_socket, size):
         """
            Check whether the receive call resulted in an error.
            :param conn_socket: A socket.
            :param size: An integer.
            :rtype: A boolean.
         """
         incoming_bytes = self.receive_bytes(conn_socket, size)
         if type(incoming_bytes) != bytes:
              return incoming_bytes, comstatus.SOCKET_ERROR 
         else: 
             self.buffer += incoming_bytes
             return True, comstatus.SUCCESS


    def read_from_buffer(self, conn_socket):
        """
            Read from the server buffer until end of message.
            :param conn_socket: A socket.
        """
        print("SERVER READING FROM BUFFER\n")
        incoming_bytes, status = self.check_incoming_bytes(conn_socket,
                                                           MESSAGE_SIZE)
        if status == comstatus.SOCKET_ERROR: 
            return incoming_bytes, None 
        else:
            start = 0
            message_len, start_pos = _DecodeVarint32(self.buffer, start)
            message_chunk = len(self.buffer) - start_pos
            if message_chunk < message_len:
                missing_bytes = message_len - message_chunk
                print("Server about to recv %s missing bytes\n" % missing_bytes)
                self.buffer = bytearray(self.buffer)
                if missing_bytes < BYTE_LIMIT:
                    incoming_bytes, status = self.check_incoming_bytes(
                                                                   conn_socket,
                                                                  missing_bytes)
                    if status == comstatus.SOCKET_ERROR:
                        return incoming_bytes, None
                else:
                    factor = int(missing_bytes/BYTE_LIMIT)
                    remainder = missing_bytes % BYTE_LIMIT
                    for _ in range(factor):
                        incoming_bytes, status = self.check_incoming_bytes(
                                                                   conn_socket,
                                                                  BYTE_LIMIT)
                        if status == comstatus.SOCKET_ERROR:
                            return incoming_bytes, None
                    # Receive the remainder.
                    if remainder > 0:
                        incoming_bytes, status = self.check_incoming_bytes(
                                                                   conn_socket,
                                                                  remainder)
                        if status == comstatus.SOCKET_ERROR:
                            return incoming_bytes, None
            self.buffer = bytes(self.buffer)
            return message_len, start_pos

    def receive_bytes(self, conn_socket, buffer_size):
        """
            Receives a response from the wire.
            :param conn_socket: A socket object.
            :param buffer_size: An int.
        """
        try:
            data_bytes = conn_socket.recv(buffer_size)
            if len(data_bytes) == 0:
                return None
            return data_bytes
        except error as socket_error:
            return socket_error

    def send(self, peer_conn, data_bytes):
        """
            Send a request to remote peer.
            :param peer_conn: A socket object.
            :param data_bytes: A serialized bytestring.
        """
        try:
            bytes_sent = peer_conn.send(data_bytes)
            print("%s Server sent %s bytes\n" % (self.userid,
                                        bytes_sent))
            return comstatus.SUCCESS, bytes_sent 
        except error as socket_error:
            return comstatus.SOCKET_ERROR, socket_error 

