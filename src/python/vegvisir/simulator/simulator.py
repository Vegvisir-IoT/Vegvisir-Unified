#!/usr/bin/python3

from time import time
from copy import deepcopy
from random import randint

from ..network.simnetwork import SimulationNetworkOperator 
from .controller import Controller
from .opcodes import Event
from .sim_helpers import get_user_locations, get_peer_id, merge_all_users
from ..blockchain.crypto import (generate_private_key, generate_keypair,
                                 generate_public_key)
from ..blockchain.authentication import Certificate, Keystore
from ..blockchain.blockdag import GenesisBlock, Blockchain
from ..blockchain.crdt import Crdt


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza, Pablo Fiori"]

SECONDS_PER_DAY = 86400
TWO_MINUTES = 120.00
FOUR_MINUTES = 240.00


# @brief: A class for simulating blockchain operations.
class Simulator(object):

    """ The representation of blockchain simulator.

        :param cab_simulator: A CabSimulator.
        :param distance: An int, the distance in neighbor search.
        :param days: An int, the number of days to simulate.
        :param quorum: An int, the number of witnesses required.
    """
    def __init__(self,  cab_simulator, distance, days, quorum):
        self.cab_sim = cab_simulator
        self.distance = int(distance[0])
        self.days = days
        self.quorum = quorum
        self.user_names = cab_simulator.cab_handler.cab_user_names
        # Useful for pointing controllers to their file descriptors
        self.cab_structs = cab_simulator.cab_handler.cab_struct
        self.merge_at_finish = False
        self.users = None
        self.network = None

    def simulate(self):
        """ Simulate the Vegvisir protocol for a number of days. """

        self.create_components()
        # Initialize network
        self.network = SimulationNetworkOperator()

        self.network.add_users_to_network(self.users)
        self.bootup_users()

        sim_secs = SECONDS_PER_DAY * self.days
        sim_limit = self.network.time + sim_secs
        print("Simulation limit will be %s\n" % sim_limit)
        
        # Sequential execution of events
        while not self.network.terminate_network:
            self.perform_timed_merges(sim_limit)
            if self.merge_at_finish:
                merge_all_users(self.users)
                self.network.terminate()
            else:
                self.network.terminate()

        # # output some results
        # self.print_all_chains(self.users)
        # self.compare_all_chains(self.users)
        # self.visualize_all_users(self.users)
        self.print_simulation_stats()
        return "\n–––––––– SIMULATION FINISHED ––––––––\n"

    def create_components(self):
        """ Creates components to be used by the simulation including
            certificates, keystore, controllers, etc.
        """
        # Generate public and private keys for the certificate authority
        ca_private_key = generate_private_key()
        ca_public_key = generate_public_key(ca_private_key)
        # Create an (arbitrarily selected) user id
        userid = 1

        # Create a new certificate for the certificate authority
        ca_cert = Certificate(userid, ca_public_key)
        # Certificate authority signs off on their own new certificate.
        ca_cert.sign(ca_private_key)
        ks = Keystore(ca_cert)

        self.users = self.chain_controller_factory(ca_private_key, ca_cert, ks)

    def chain_controller_factory(self, ca_private_key, ca_cert, ks):
        """ Generate controllers for devices in the network.

            :param ca_private_key: An _EllipticCurvePrivateKey.
            :param ca_cert: A Certificate.
            :param ks: A Keystore.
        """

        cert_list = []
        key_list = []
        user_list = []
        for x in range(len(self.user_names)):
            # Generate a public and private key for the user certificate.
            private_key, public_key = generate_keypair()
            userid = self.user_names[x]
            # Create a certificate for a user.
            cert = Certificate(userid, public_key)
            # Certificate authority has to sign off on new user certificate.
            cert.sign(ca_private_key)
            cert_list.append(cert)
            key_list.append(private_key)

        # timestamp = self.network.time
        # Generate the genesis block with a list of all certifications.
        gblock = GenesisBlock(1, 0.000, ca_cert, cert_list)
        # Certificate authority signs off on the Genesis block.
        gblock.sign(ca_private_key)
        crdt = Crdt()
        chain = Blockchain(gblock, ks, crdt)

        for i in range(len(self.user_names)):
            new_chain = deepcopy(chain)
            user = Controller(self.user_names[i], new_chain, key_list[i],
                              self.cab_sim, self.quorum)
            user_list.append(user)

        return user_list

    def bootup_users(self):
        """ Give each user a starting block to get the network going. """

        for user in self.users:
            user.process_event((time(), ['god', 'DontMatter',
                                         Event.RECORD_REQUEST], ['blah']))

    def perform_timed_merges(self, sim_secs):
        """
            Performs merges between users and makes new requests as dictated
            by the simulations.
            @param sim_secs: A float, the total number of secs to be simulated.
        """
        old_time = self.network.time
        self.network.time += TWO_MINUTES
        while (self.network.time <= sim_secs):
            time_delta = self.network.time - old_time
            
            # Create a request every two minutes
            if time_delta < TWO_MINUTES:
                self.network.time += (TWO_MINUTES - time_delta)
                old_time = self.network.time
            else:
                old_time = self.network.time
 
            rnd_user, locations = get_user_locations(self.users)
            rnd_user[0].process_event(("bleh", ['god', rnd_user[0],
                                                Event.RECORD_REQUEST]))


            # Reconcile two nodes every four minutes
            rnd_user, locations = get_user_locations(self.users)


            near_by_peers = self.cab_sim.find_neighbors(rnd_user[1],
                                                        locations,
                                                        self.distance)
            if len(near_by_peers) > 1:
                # print("FOUND 1+ NEARBY USERS AT TIME %s\n" % self.network.time)

                # Choose a random peer
                peer_list = near_by_peers.keys()
                rnd_peer_ctl = get_peer_id(peer_list, self.users)

                # Submit a connection event
                event_type = Event.CONNECTION_BUILT
                rnd_user[0].process_event(("bleh", [rnd_peer_ctl,
                                                    rnd_user[0],
                                                    event_type]))

            elif len(near_by_peers) == 1:
                # print("FOUND 1 NEARBY USER AT TIME %s\n" % self.network.time)
                peer_list = near_by_peers.keys()
                rnd_peer_ctl = get_peer_id(peer_list, self.users,
                                            singleton=True)

                # Submit a connection event
                event_type = Event.CONNECTION_BUILT
                rnd_user[0].process_event(("bleh", [rnd_peer_ctl,
                                                    rnd_user[0],
                                                    event_type]))

    def print_simulation_stats(self):
        """
            Prints the simulation statistics of each controller. """

        block_percentages = {}
        user_len = len(self.users)
        for user1 in self.users:
            for block_hash, block in user1.blockchain.blocks.items():
                if not isinstance(block, GenesisBlock):
                    block_count = 0
                    for user2 in self.users:
                        if user1.userid != user2.userid:
                            if block_hash in user2.blockchain.blocks.keys():
                                block_count += 1
                    if block_hash not in block_percentages.keys():
                        percentage_shared = (block_count/user_len) * 100
                        if b'Type: POW' in block.tx[0].comment:
                            block_stat = [percentage_shared, 'POW']
                            block_percentages[block_hash] = block_stat
                        else:
                            block_stat = [percentage_shared, 'REQ']
                            block_percentages[block_hash] = block_stat
        self.print_block_quorum_status()
        self.print_reconciliation_stats()

        # self.output_most_spread_blocks(block_percentages)
        # self.output_block_propagations()

    def print_reconciliation_stats(self):
        """ Print the simulated reconciliation times. """

        print("----- RECONCILIATION RESULTS -----\n\n")
        total_system_rec = 0
        for user in self.users:
            print("User -> %s" % user.userid)
            for peer, reconcilations in user.reconciliations.items():
                print("Reconcilations with peer %s" % peer.userid)
                total_peer_rec = 0
                all_recs = len(reconcilations)
                for rec in reconcilations:
                    total_peer_rec += rec['recon_time']
                    print("Missing blocks %s, Total rec time %s secs" % (
                     rec['missing_blocks'], rec['recon_time']))
                avg_peer_rec_time = float(total_peer_rec / all_recs)
                print("Average rec time with "
                      "peer %s -> %s" % (peer.userid, avg_peer_rec_time))
                total_system_rec += avg_peer_rec_time
                print("\n")
            print("\n\n")
        system_avg = total_system_rec / len(self.users)
        print("AVERAGE SYSTEM RECONCILIATION TIME %s \n" % system_avg)

    def print_block_quorum_status(self):
        """ Print the status of each block on the chain w.r.t POWs. """
        short_block_ids = []

        for user in self.users:
            satisfied_blocks = len(user.quorum_mappings)
            block_count = len(user.blockchain.blocks)
            block_status_message = "User -> " + user.userid
            block_status_message += ", Blocks created -> "
            block_status_message += str(block_count) + "\n"
            for b_hash in user.blockchain.blocks:
                temp_id = randint(1, pow(10, 10))
                while temp_id in short_block_ids:
                    temp_id = randint(1, pow(10, 10))
                block_status_message += str(temp_id) + " : "
                if b_hash in user.quorum_mappings:
                    quorum_times = user.quorum_mappings[b_hash]
                    t_time = str(quorum_times[1] - quorum_times[0])
                    block_status_message += t_time + " seconds "
                    block_status_message += "to meet quorum \n"
                else:
                    block_status_message += "0 \n"
            quorum_percentage = (satisfied_blocks / block_count) * 100
            block_status_message += "Percentage of blocks shared: "
            block_status_message += str(quorum_percentage) + "\n"
            if satisfied_blocks > 0:
                all_quorums = sum([x[1] - x[0] for x in  \
                    user.quorum_mappings.values()])
                avg_t_to_quorum = int((all_quorums / satisfied_blocks))
                block_status_message += "Average time to meet quorum: "
                block_status_message += str(avg_t_to_quorum)
            print(block_status_message)
            print("\n\n")
