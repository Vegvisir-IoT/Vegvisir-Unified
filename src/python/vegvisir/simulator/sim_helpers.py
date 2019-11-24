from random import randint, seed
from time import time
from argparse import ArgumentParser

from .opcodes import Event

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

# Set seed for replicable experiments
seed(10)


def get_user_locations(users):
    """ Retrieves the locations of controllers.

        :param users: A list of Controllers.
    """
    all_users = len(users)
    random_user_id = randint(0, all_users-1)
    random_user_location = None
    ctl_id = None
    locations = {}
    for idx in range(all_users):
        user_id = users[idx].userid
        location = users[idx].location
        locations[user_id] = location
        if idx == random_user_id:
            random_user_location = location
            ctl_id = users[idx]
    return [ctl_id, random_user_location], locations


def get_peer_id(peer_list, users, singleton=False):
        """
            Retrieves the controller associated with a user id.
            If there are multiple options, choose a random one and find the
            associated controller.
            If there is only one option, find the controller associated with
            the user_id.
            :param peer_list: A list, peer cab ids.
            :param users: A list of Controllers.
            :param singleton: A boolean.
        """

        if not singleton:
            start = 0
            stop = randint(0, len(peer_list)-1)
            peer_of_interest = None
            for peer in peer_list:
                if start == stop:
                    peer_of_interest = peer
                    break
                else:
                    start += 1

            for user in users:
                if user.userid == peer_of_interest:
                    return user
        else:
            for peer in peer_list:
                peer_of_interest = peer
                break
            for user in users:
                if user.userid == peer_of_interest:
                    return user


def merge_all_users(user_list, full_merge=True):
    """ Merge all users participating in Vegvisir protocol.

        :param user_list: A list of Controllers.
        :param full_merge: A boolean.
    """
    print("\n\n\nSTARTING MERGE OF ALL USERS\n\n\n")
    if not full_merge:
        print("OPERATING ON PARTIAL MERGE\n")
        total_users = len(user_list)
        print("User list length is %s\n" % total_users)
        start = time()
        for i in range(total_users):
            if (total_users - i >= int(total_users/4)):
                for user2 in user_list[i+1:]:
                    print("Processing another peer "
                          "for %s\n" % user_list[i].userid)
                    if user_list[i].userid != user2.userid:
                        user_list[i].process_event((time(), [user2,
                                                             user_list[i],
                                                    Event.CONNECTION_BUILT]))
                print("Done processing user %s at "
                      "index %d\n" % (user_list[i].userid, i))
        end_time = time() - start
        print("TOTAL PARTIAL MERGE TIME %s\n" % end_time)
    else:
        print("OPERATING ON FULL MERGE\n")
        for user1 in user_list:
            for user2 in user_list:
                if user1.userid != user2.userid:
                    user1.process_event((time(), [user2,
                                                  user1,
                                                  Event.CONNECTION_BUILT]))


def sanitize_filename(name):
    """
        Removes newline chars from a file name returned from
        a script ru.
        @param name: the filename
    """
    name = name.strip('$\n')
    return name


def parseargs():
    """ Parse the arguments passed by the user. """
    distance_help_message = ("Distance for peer search\n \
                             Either in feet for Haversine search \
                             or decimal numbers for Min-Max search\n \
                             E.g. 150ft in Haversine is equivalent to \
                             0.00006 in Min-Max\n \
                             You many specify a list distances to be used \
                             for search during an experiment \
                             i.e -d 150, 300\n")

    update_bound_message = ("The total number of updates to be used for \
                            filtering cabs, default is 20,000")

    method_help_message = ("Method for finding nearby peers, either \
                            'haversine' or 'min-max, default is haversine")

    days_help_message = ("The number of days to simulate for each method \
                          passed in as argument i.e. [1, 2 ,5], default is 1")

    quorum_message = ("The number of witnesses needed for each block")

    parser = ArgumentParser(description='Retrivies arguments for simulations')

    parser.add_argument('-d', '--distance', type=str, nargs='+',
                        help=distance_help_message, default=20000)
    parser.add_argument('-u', '--users', type=int, nargs='+',
                        help='The number of cabs to simulate i.e. [10, 50]',
                        required=True)
    parser.add_argument('-b', '--update_bound', type=int,
                        help=update_bound_message, default=20000)
    parser.add_argument('-m', '--method', type=str,
                        help=method_help_message,
                        choices=['haversine', 'min-max'])
    parser.add_argument('-t', '--time', type=int, nargs='+',
                        help=days_help_message, default=1)
    parser.add_argument('-q', '--quorum', type=int, help=quorum_message,
                        default=2)

    args = parser.parse_args()

    # Assign to variables
    distances = list(args.distance)
    users = args.users
    total_updates = args.update_bound
    method = args.method
    time = args.time
    quorum = args.quorum

    if method is 'haversine' and any(x < 150 for x in distances):
        print("No distance arg can be less than 150ft when \
               search method is haversine!\n")
        exit(1)

    for dist in distances:
        temp = float(dist)
        if temp > 1.0:
            dist = int(dist)
        else:
            dist = temp
    return distances, users, total_updates, method, time, quorum
