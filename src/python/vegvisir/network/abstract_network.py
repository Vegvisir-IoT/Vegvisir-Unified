from abc import ABCMeta, abstractmethod
from socket import SHUT_RDWR
from queue import Queue


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


#@brief: An abstract class that simulation and emulation networks use. 
class Network(metaclass=ABCMeta):

    """
       :param incoming_connections: A dictionary.
    """
    def __init__(self, userid=None, incoming_connections={}):
        self.incoming_connections = incoming_connections
        self.outgoing_connections = {}
        self.userid = userid
        self.inputs = []
        self.outputs = []
        self.message_queues = {}

    @abstractmethod
    def send(self, payload,  sender=None, destination=None, request_type=None):
        pass

    @abstractmethod
    def receive(self, sender=None):
        pass 

    def add_connection(self, connection, address=None, port=None,
                       outgoing=False):
        """
           Add an incoming connection to the lookup dictionary.
           :param connection: A socket object.
           :param address: A tuple.
           :param port: An int.
           :param outgoinga: A boolean.
        """
        self.inputs.append(connection)
        self.outputs.append(connection)
        self.message_queues[connection] = Queue()
        if outgoing:
            self.outgoing_connections[port] = connection 
        else:
            self.incoming_connections[connection] = address


    def remove_connection(self, connection):
        """
           Remove an active connection from the lookup table.
           :param connection: A socket object.
        """
        if connection in self.incoming_connections:
            print("Closing incoming connection to %s \n" % connection)
        if connection in self.incoming_connections:
            del self.incoming_connections[connection]
            self.inputs.remove(connection)
        else:
            port = -1
            for out_port, con in self.outgoing_connections.items():
                if con == connection:
                    port = out_port
            del self.outgoing_connections[port]
            self.inputs.remove(connection)
        if connection in self.message_queues:
            del self.message_queues[connection]
        connection.shutdown(SHUT_RDWR)
        connection.close()
