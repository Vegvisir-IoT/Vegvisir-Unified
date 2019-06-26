from abc import ABCMeta, abstractmethod
from socket import SHUT_RDWR


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


#@brief: An abstract class that simulation and emulation networks use. 
class Network(metaclass=ABCMeta):

    """
       :param active_connections: A dictionary.
    """
    def __init__(self, userid=None, active_connections={}):
        self.active_connections = active_connections
        self.userid = userid
        self.inputs = []

    @abstractmethod
    def send(self, payload,  sender=None, destination=None, request_type=None):
        pass

    @abstractmethod
    def receive(self, sender=None):
        pass 

    def add_connection(self, connection, address):
        """
           Add an incoming connection to the lookup dictionary.
           :param connection: A socket object.
           :param address: A tuple.
        """
        self.inputs.append(connection)
        self.active_connections[connection] = address


    def remove_connection(self, connection):
        """
           Remove an active connection from the lookup table.
           :param connection: A socket object.
        """
        print("%s closing socket connection....\n" % self.userid)
        connection.shutdown(SHUT_RDWR)
        connection.close()
        if connection in self.active_connections:
             del self.active_connections[connection]
             self.inputs.remove(connection)
