from select import select
from queue import Empty as queue_is_empty


from vegvisir.blockchain.blockdag import GenesisBlock, Blockchain
from vegvisir.blockchain.authentication import Certificate, Keystore
from vegvisir.blockchain.crdt import Crdt
from vegvisir.emulator.server import VegvisirServer
from vegvisir.emulator.client import VegvisirClient
#from vegvisir.emulator.serverside_controller import ServerController
from vegvisir.emulator.stateless_request_handler import PeerRequestHandler
from vegvisir.emulator.stateless_request_creator import ProtocolRequestCreator
from vegvisir.emulator.gossip_layer import GossipLayer 
from vegvisir.emulator.emulation_helpers import deserialize_certificate
from vegvisir.emulator.stateless.state_machine import StateMachine
from vegvisir.emulator.socket_opcodes import (ProtocolState as rstate,
                                             CommunicationStatus as comstatus)
from vegvisir.network.emulation_network import EmulationNetworkOperator
from vegvisir.proto import vegvisir_pb2 as vegvisir 
from vegvisir.protocols.vector.vector import VectorClock
from vegvisir.protocols.vector.vectorserver import VectorServer
from vegvisir.protocols.sendall.sendallserver import SendallServer
from vegvisir.protocols.frontier.stateless_frontier_client import FrontierClient
from vegvisir.protocols.frontier.stateless_frontier_server import FrontierServer
from vegvisir.protocols.frontier.frontier_handler import FrontierHandler
from vegvisir.protocols.handshake.handshake_handler import HandshakeHandler


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


SOCKET_TIMEOUT = 2 


# @brief A module for emulating the vegvisir protocol on each node
class Emulator(object):

    """
       The representation of a vegvisir emulation.
       :param args: A set of parameters for loading the blockchain.
    """
    def __init__(self, args):
      self.userid = args['username']
      self.blockchain_file = args['chainfile']
      self.collective_params = args['paramsfile']
      self.crash_prob = args['crash_prob']
      self.block_limit = args['block_limit']
      self.protocol = args['protocol']


    def activate_gossip_layer(self):
        """
            Emulates the vegvisir protocol running on one node.
        """
        port, peer_names, params = self.read_parameters()
        self.users = peer_names
    
        self.read_blockchain_file()
    
        self.create_components(port, params)


    def run_core(self):
    
        # Start the protocol runner
        self.spin_server_forever()
    
    
    def create_components(self, port, params):
        """
           Create all the components needed for a successful emulation.
           :param port: An integer.
           :param params: A dictionary.
        """
        # Create the Vector clock to be used by the request handler and emulator.
        vector_clock = VectorClock(self.userid, self.users, self.gblock.hash(),
                                   self.blockchain) 
    
        # Create server and client.
        server = VegvisirServer(port, self.userid)
        client = VegvisirClient(self.userid)
    
        # Create the network operator.
        self.network = EmulationNetworkOperator(server, client)
    
        # Create the request handlers and creators.
        self.request_handler = PeerRequestHandler(self.blockchain,
                                                  vector_clock, self.protocol)
        self.request_creator = ProtocolRequestCreator(self.blockchain, self.network, self.private_key)
    
    
        # Create the sendall protocol server.
        sendall_server = SendallServer(self.request_handler, self.crash_prob)
    
        # Create the frontier protocol client and server.
        frontier_client =  FrontierClient(self.request_creator,
                                          self.request_handler,self.crash_prob)
    
        frontier_server = FrontierServer(self.request_creator,
                                         self.request_handler,self.crash_prob)
    
        # Create the handshake handler for the handshake protocol.
        handshake_handler = HandshakeHandler(frontier_server)
    
        # Create the frontier message handler.
        frontier_handler = FrontierHandler(self.private_key, frontier_server,
                                           frontier_client)
    
        # Create the vector server to be used by the state machine.
        vector_server = VectorServer(self.request_creator,
                                     self.request_handler, vector_clock,
                                     self.crash_prob)
    
        # Create the state machine for processing messages.
        self.state_machine = StateMachine(self.network, frontier_handler, frontier_server,
                                     sendall_server, vector_server,
                                     handshake_handler) 
    
    
        # Create the emulator for sleeping and waking up
        self.gossip_handler = GossipLayer(self.network, self.private_key,
                                          params, self.block_limit,
                                          self.request_handler,
                                          self.request_creator)
        
    
    def read_parameters(self):
        """
           Read network parameters from a file.
           TO-DO: Make this dynamic as protocol runs.
        """
        fd = open(self.collective_params, "r")
        found_user = False
        peer_names = []
        line = fd.readline()
        while line:
            params = line.split(",")
            peer_names.append(params[0])
            if params[0] == self.userid:
                found_user = True
                my_port = int(params[2])
                peers = []
                start = 3
                while start < len(params):
                    peer = {'hostname' : params[start],
                            'port' : int(params[start + 1])}
                    peers.append(peer)
                    start += 2
            line = fd.readline()
        if not found_user:
            print("%s user not found!\n" % self.userid)
            exit(1)
    
        print("Username %s found!\n" % self.userid)
        print("Peer names %s\n" % peer_names)
        return my_port, peer_names, peers
     
    
    def read_blockchain_file(self):
        """
           :param chainfile: A string.
        """
        
        # Read main blockchain parameters from file
        fd = open(self.blockchain_file, "rb")
        blockchain = vegvisir.Blockchain()
        blockchain.ParseFromString(fd.read())
    
        # Copy genesis parameters and private_key
        genesis = vegvisir.Block.GenesisBlock()
        genesis.CopyFrom(blockchain.genesis)
        self.gblock, self.private_key = self.recreate_genesis(genesis)
    
        # Copy keystore
        keystore = vegvisir.Blockchain.Keystore()
        keystore.CopyFrom(blockchain.keystore)
        ks = self.recreate_keystore(keystore)
    
        # Recreate the blockchain
        fset_crdt = Crdt()
        self.blockchain = Blockchain(self.gblock, ks, fset_crdt)
        
        # Add the genesis block as the start frontier hash
        self.blockchain.crdt.addset.add(self.gblock.hash())
    
    
    def spin_server_forever(self):
        """
           Listen for new connections and process new data.
        """
        server = self.network.server
        server.bind_server()
    
        # Initialize inputs and outputs for selector
        self.network.inputs.append(self.network.server.server_socket)
        error_statuses = [comstatus.SOCKET_ERROR, comstatus.NO_DATA]
    
        while self.network.inputs:
            readable, writable, exceptional = select(self.network.inputs,
                                                     self.network.outputs,
                                                     self.network.inputs, 1)
            for incoming in readable:
                print("Readables %s\n" % readable)
                if incoming is server.server_socket:
                    print("INCOMING AT SERVER SOCKET %s\n" % server.userid)
                    connection, client_address = incoming.accept()
                    print("%s : New connection with peer %s at port %s\n" \
                          % (server.userid, client_address[0], 
                                  client_address[1]))
                    connection.settimeout(SOCKET_TIMEOUT)
                    self.network.add_connection(connection, client_address)
                    print("Adding %s to inputs\n" % connection)
                else:
                    payload = self.network.receive(incoming) 
                    print("Payload \n %s\n" % payload)
                    if payload in error_statuses:
                        self.state_machine.destroy_session(incoming)
                        if incoming in self.network.outputs:
                            self.network.outputs.remove(incoming)
                    else:
                        if not incoming in self.network.outputs:
                            self.network.outputs.append(incoming)
                        self.state_machine.process_message(payload, incoming)
                        print("Processing message is done %s\n" % server.userid)
            for ready in writable:
                if ready in self.network.message_queues:
                    try:
                        outgoing_msg = self.network.message_queues[ready].get_nowait()
                    except queue_is_empty:
                        print("No message in %s's queue atm" % ready)
                        self.network.outputs.remove(ready)
                    else:
                        status = self.network.send(outgoing_msg, ready)
                        print("Sending status %s\n" % status)
                elif ready in self.network.outputs:
                     self.network.outputs.remove(ready)
            for exception in exceptional:
                print("Connection to %s ran into an exception\n" % exception.getpeername())
                if exception in self.network.outputs:
                    self.network.outputs.remove(exception)
                self.state_machine.destroy_session(exception)
    
            # Gossip only if we have not initiated another gossip round.
            if len(self.network.outgoing_connections) < 1:
                print("No outgoing connections yet..\n")
                self.gossip_handler.random_sleep()
                if self.gossip_handler.block_limit > 0:
                    self.gossip_handler.generate_new_block()
                gossip_status, connection = self.gossip_handler.initiate_gossip()
                if gossip_status == rstate.HANDSHAKE:
                    print("%s GOSSIP STARTED SUCCESSFULLY!\n" % server.userid)
                    self.state_machine.update_state(connection, rstate.HANDSHAKE)
                else:
                    print("%s GOSSIPING FAILED, STATUS %s\n" %
                          (server.userid, gossip_status))
    
    
    def recreate_genesis(self, genesis):
        """
            Recreate the genesis block from a block read in from a file.
            :param genesis: A vegvisir.proto.Blockchain.GenesisBlock object.
            :rtype: A list of a GenesisBlock and a private_key.
        """
        gen_userid = int(genesis.userid)
        gen_timestamp = genesis.timestamp
        ca_cert, _ = deserialize_certificate(genesis.ca_certificate, ca_cert=True,
                                          genesis=True)
        cert_list = []
        for cert in genesis.certlist:
            deserialized_cert, potential_key = deserialize_certificate(cert)
            if deserialized_cert.userid == self.userid:
                my_private_key = potential_key
            cert_list.append(deserialized_cert)
    
        # Create genesis_block
        gblock = GenesisBlock(gen_userid, gen_timestamp, ca_cert, cert_list)
        gblock.sig = genesis.signature
        return gblock, my_private_key
    
    
    def recreate_keystore(self, keystore):
        """
            Recreate the keystore from a keystore read in from a file.
            :param keystore: A protos.charlottewrapper.Blockchain.Keystore.
            :rtype: A blockchain.authentication.Keystore.
        """
        ca_cert, _ = deserialize_certificate(keystore.ca_cert, ca_cert=True,
                                          genesis=True)
        ks = Keystore(ca_cert)
        for index in range(len(keystore.userids)):
            userid = keystore.userids[index].userid
            if userid != ca_cert.userid:
                current_cert = keystore.usercerts[index]
                ks.certs[userid], _ = deserialize_certificate(current_cert)
        return ks
    
