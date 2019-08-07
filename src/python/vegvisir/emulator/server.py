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


