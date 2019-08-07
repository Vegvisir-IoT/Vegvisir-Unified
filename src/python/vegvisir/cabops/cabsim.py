from random import randint

from .cab_helpers import create_transactions

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class for simulating transactions created by cabs.
class CabSimulator(object):

    """ A representation of a cab simulator.

        :param triage: A CabData.
        :param spy: A Neighbors.
        :param method: A boolean.
    """
    def __init__(self, triage, spy, method):
        self.cab_handler = triage
        self.data_path = self.cab_handler.data_path
        self.neighbor_finder = spy
        self.haversine = method

    def find_neighbors(self, user_loc, peer_locs, distance):
        """ Find cab ids of neighbors that the user can contact for
            reconciliation purposes.

            :param user_loc: A dictionary, contains id, longitude, latitude.
            :param peer_locs: A dictionary of locations.
            :param distance: A float.
        """
        if self.haversine:
            nearby_users = self.neighbor_finder.find_by_haversine(user_loc, 
                            peer_locs, distance)
        else:
            nearby_users = self.neighbor_finder.find_by_normalization(user_loc,
                            peer_locs, distance)

        return nearby_users

    def simulate_transactions(self, total):
        """ Create a random amount of transactions for each user.

            :param total: An int, the total number of transactions.
        """
        new_data = {}
        for cab_id, cab_data in self.cab_handler.cab_struct.items():
            fd = cab_data[1]

            usr_block_data = create_transactions(cab_id, fd, randint(1,total))
            new_data[cab_id] = usr_block_data  

        return new_data

    def simulate_requests(self, cab_id, cab_fd, total):
        """ Create transactions for a block.

            :param cab_id: A string.
            :param cab_fd: A file descriptor.
            :param total: An int.
        """
        limit = randint(1, total)
        # print("Simulating the creation of %d txs" % limit)
        block_data = create_transactions(cab_id, cab_fd, limit)

        return block_data
