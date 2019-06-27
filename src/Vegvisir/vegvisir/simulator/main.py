#!/usr/bin/python3

from time import time
from sys import path
from subprocess import TimeoutExpired, run, CalledProcessError, PIPE
from random import seed, randint

from vegvisir.cabops.cabsim import CabSimulator
from vegvisir.cabops.cab_data_processor import CabData
from vegvisir.cabops.distance_aggregation import Neighbors
from vegvisir.simulator.simulator import Simulator
from vegvisir.simulator.sim_helpers import parseargs, sanitize_filename


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


def run_experiments(args):
    """ Run a simulation for different user categories.

        :param args: A list of arguments for the experiment including
                     a list of days, the search method, a list of
                     distances, the number of updates for filtering
                     cabs, and the quorum for number of witnesses.
    """
    day = str(args[0])
    method = args[1]
    user_categories = args[2]
    distances = args[3]
    total_updates = args[4]
    quorum = args[5]

    try:
        dir_name = day + "Days"
        day_dir_name = run(["./dir_creation.sh", dir_name], stdout=PIPE,
                           check=True, universal_newlines=True).stdout
        day_dir_name = sanitize_filename(day_dir_name)
    except CalledProcessError as cpe:
        print("Directory creation failed!\n")
        print("Return code was -> %d\n" % cpe.returncode)

    m_dir_name = run(["./dir_creation.sh", method], stdout=PIPE,
                     check=True, universal_newlines=True).stdout
    m_dir_name = sanitize_filename(m_dir_name)

    for user in user_categories:
        u_dir_name = run(["./dir_creation.sh", str(user)], stdout=PIPE,
                         check=True, universal_newlines=True).stdout
        u_dir_name = sanitize_filename(u_dir_name)
        if method == 'haversine':
            for dist in distances:
                file_name = run(["./file_creation.sh", str(user), method, dist,
                                 day], stdout=PIPE, check=True,
                                universal_newlines=True).stdout
                file_name = sanitize_filename(file_name)
                try:
                    fd = open(str(file_name), "w")
                    run(["./simulate_distance.py", "-d", dist, "-u", str(user),
                         "-m", method, "-t", day, '-b', str(total_updates),
                         "-q", str(quorum)], stdout=fd)
                    fd.close()
                except CalledProcessError as cpe:
                    print("Running experiment failed\n")
                    print("Code returned is %s\n" % cpe.returncode)
                    print("Output is %s\n" % cpe.output)
                run(["mv", file_name, u_dir_name])
        else:
            for dist in distances:
                file_name = run(["./file_creation.sh", str(user), method, dist,
                                 day], stdout=PIPE, check=True,
                                universal_newlines=True).stdout
                file_name = sanitize_filename(file_name)
                try:
                    fd = open(str(file_name), "w")
                    run(["./simulate_distance.py", "-d", dist, "-u", str(user),
                         "-m", method, "-t", day, '-b', total_updates,
                         "-q", str(quorum)], stdout=fd)
                    fd.close()
                except CalledProcessError as cpe:
                    print("Running experiment failed\n")
                    print("Code returned is %s\n" % cpe.returncode)
                    print("Output is %s\n" % cpe.output)
                run(["mv", file_name, u_dir_name])
        run(["mv", u_dir_name, m_dir_name])
    run(["mv", m_dir_name, day_dir_name])


if __name__ == '__main__':
    distances, users, total_updates, method, days, quorum = parseargs()

    seed(10)
    for day in days:
        run_experiments([day, method, users, distances, total_updates, quorum])
