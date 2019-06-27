#!/usr/bin/python3

from sys import argv as arg_vector
from time import time
from random import randint

from vegvisir.cabops.cabsim import CabSimulator
from vegvisir.cabops.cab_data_processor import CabData
from vegvisir.cabops.distance_aggregation import Neighbors
from vegvisir.simulator.simulator import Simulator
from vegvisir.simulator.sim_helpers import parseargs

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


def simulate_distance(dist, num_users, method, days, update_bound, quorum):
    """ Simulates the Vegvisir protocol for a given distance.

        :param dist: An int.
        :param num_users: An int.
        :param method: A string.
        :param days: An int.
        :param quorum: An int, the number of witnesses required.
    """
    cab_data = CabData('_cabs.txt')

    extremes = cab_data.read_data_extremes('extremes.txt')

    neighbor_finder = Neighbors(extremes['max_lat'], extremes['max_long'],
                                extremes['min_lat'], extremes['min_long'])

    cab_data.cab_triage(update_bound, num_users)

    if method == 'haversine':
        sim = CabSimulator(cab_data, neighbor_finder, True)
    else:
        sim = CabSimulator(cab_data, neighbor_finder, False)

    simulation = Simulator(sim, distance, days, quorum)
    simulation.simulate()

    # Close all open files.
    sim.cab_handler.close_file_descriptors()


if __name__ == '__main__':
    distance, num_users, update_bound, method, days, quorum = parseargs()

    simulate_distance(distance[0], num_users[0], method, days[0],
                      update_bound, quorum)
