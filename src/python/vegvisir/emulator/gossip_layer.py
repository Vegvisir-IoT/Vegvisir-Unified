from time import time, sleep
from copy import deepcopy
from random import randint

from .emulation_helpers import (create_blocks, update_local_vector_maps)
from .socket_opcodes import (ProtocolState as rstate,
                              CommunicationStatus as comstatus)
from vegvisir.simulator.opcodes import Operation
import vegvisir.proto.handshake_pb2 as hs 


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles gossiping. 
class GossipLayer(object):

    """
        The representation of the layer for contacting peers.
        :param network: An EmulationNetworkOperator object.
        :param private_key: An _EllipticCurvePrivateKey for signing blocks.
        :param peers: A list of peers.
        :param block_limit: An int.
        :request_handler: A PeerRequestHandler object.
        :request_creator: A PeerRequestCreator object.
    """
    def __init__(self, network, private_key, peers, block_limit, request_handler,
                 request_creator):
        self.private_key = private_key
        self.peers = peers
        self.block_limit = block_limit
        self.request_creator = request_creator
        self.userid = request_handler.userid
        self.blockchain = request_handler.blockchain
        self.network = network
        self.vector_clock = request_handler.vector_clock
        self.protocol = request_handler.protocol


    def random_sleep(self):
        """
            Sleep a random amount of time between 0 and 1 second.
        """
        sleepy_time = randint(0, 2)
        sleep(sleepy_time)
        print("%s woke up from %s sec nap!\n" % (self.userid, sleepy_time))


    def initiate_gossip(self):
        """
            Wake up from sleep and contact a random peer.
            :param inputs: A list of file descriptors.
            :rtype: A ProtocolStatus enum.
        """
        if len(self.blockchain.blocks) == 1:
            return "Nothing to gossip\n", None

        print("%s is ready to gossip!\n" % self.userid)
        random_idx = randint(0, (len(self.peers) - 1))
        peer_hostname = self.peers[random_idx]['hostname']
        peer_port = self.peers[random_idx]['port']
        active_connections = self.network.outgoing_connections
        if not (peer_port in active_connections):
            connection = self.network.client.connect_to_peer(
                                                  peer_hostname,
                                                  peer_port)

            if connection != comstatus.SOCKET_ERROR:
                print("User %s, successfully connected to peer at \
                      port %s\n" % (self.userid, peer_port))
                self.network.add_connection(connection,
                                            port=peer_port,
                                            outgoing=True)
                message_queue = self.network.message_queues[connection]
                self.initiate_protocol(message_queue,
                                       hs.HandshakeMessage.REQUEST) 
                return rstate.HANDSHAKE, connection
            else:
                print("Connection to %s failed.... \n" % peer_port)
                return rstate.CONNECTION_ATTEMPT_FAILURE, None
        else:
            existing_conn = self.network.outgoing_connections[peer_port]
            message_queue = self.network.message_queues[existing_conn] 
            self.initiate_protocol(message_queue, hs.HandshakeMessage.REQUEST)
            return rstate.HANDSHAKE, existing_conn


    def initiate_protocol(self, message_queue, request_type):
        """
            Initiate the Vegvisir protocol after connecting to a peer.
            The initial message sent should have a list of protocols that 
            the client can speak. 
            :param message_queue: A Queue object.
            :param request_type: A ProtocolVersion enum.
        """
        request = self.request_creator.initiate_protocol_request(
                                                            self.protocol,
                                                            request_type)
        message_queue.put(request)


    def generate_new_block(self):
        """
            Generates a few new blocks after waking up from sleep.
        """
        print("%s is generating %s new blocks!\n" % (self.userid,
                                                  self.block_limit)) 
        block_list = create_blocks(self.userid, list(self.blockchain.frontier_nodes),
                     self.private_key, self.block_limit)
        for block in block_list:
            self.blockchain.add(block, Operation.ADDED_REQUEST)
            #self.blockchain.set_changed()
            #self.blockchain.notify_observers(block)
            print("Newly minted block hash %s\n" % block.hash())
            if self.protocol == hs.VECTOR:
                update_local_vector_maps(self.vector_clock, block)
            if self.vector_clock.offline_activity == False:
                self.vector_clock.update_offline_activity(True)

