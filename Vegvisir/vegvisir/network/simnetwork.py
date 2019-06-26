from heapq import heappush, heappop
from random import randint

from ..simulator.opcodes import Event
from .abstract_network import Network

__author__ = "Gloire Rubambiza, Pablo Fiori"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza, Pablo Fiori"]


# @brief: A class for simulating network delays
class SimulationNetworkOperator(Network):


    def __init__(self):
        self.priority_heap = []
        self.members = {}
        self.terminate_network = False
        self.time = 1000000000.000

    def send(self, payload, sender, destination):
        """ Send an event over the network by placing it in the queue. """

        transit_time = randint(1, 300)/100 + self.time
        try:
            heappush(self.priority_heap, (transit_time, [sender, destination] +
                                          payload))
        except TypeError as te:
            print("Two transit times caused a problem, nettime -> %d and "
                  "transit -> %d\n" % (self.time, transit_time))
        return transit_time

    def receive(self):
        """ Fulfills a request that was put in the queue by a device. """

        while len(self.priority_heap) > 0:
            event = heappop(self.priority_heap)
            self.time = event[0]
            self.process_event(event)

    def print_event(self, event):
        """ Print the type of event being processed. """

        if event[1][2] == Event.PEER_REQUEST:
            print("PEER REQUEST EVENT -> %s " % str([event[0]]+event[1][:4]))
        else:
            print("OTHER EVENT -> %s " % str([event[0]]+event[1][:3]))

    def process_event(self, event):
        """ Process an event popped from the queue.

            :param event: A tuple, transit time and list of event info.
        """

        peer = event[1][1]
        peer.process_event(event)

    def add_users_to_network(self, user_list):
        """ Add a list of users to the network for processing their events.
            :param user_list: A list of Controllers
        """
        for user in user_list:
            user.network = self
            self.members[user.userid] = user

    def terminate(self):
        """ Terminate the network when event queue is depleted. """

        self.terminate_network = True
