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
        self.buffer = bytearray()

    def connect_to_peer(self, host, port):
        """
            Connect to a remote peer.
            :param host: A string of the peer's ip address.
            :param port: An int.
        """
        try:
            new_socket = socket(AF_INET, SOCK_STREAM)
            new_socket.settimeout(SOCKET_TIMEOUT)
            new_socket.connect((host, port))
            return new_socket 
        except error as socket_error:
            return comstatus.SOCKET_ERROR


