from select import select
from queue import Empty as queue_is_empty


from vegvisir.blockchain.blockdag import GenesisBlock, Blockchain
from vegvisir.blockchain.authentication import Certificate, Keystore
from vegvisir.blockchain.crdt import Crdt
from vegvisir.emulator.server import VegvisirServer
from vegvisir.emulator.client import VegvisirClient
from vegvisir.emulator.serverside_controller import ServerController
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


    def emulate_vegvisir(self, args):
        """
            Emulates the vegvisir protocol running on one node.
            :param args: A dictionary of parameters to start the protocol.
        """
        my_userid = args['username']
        genesis_block_file = args['chainfile']
        collective_params = args['paramsfile']
        crash_prob = args['crash_prob']
        block_limit = args['block_limit']
        protocol = args['protocol'] 
    
        port, peer_names, params = self.read_parameters()
    
        gblock, blockchain, private_key = self.read_blockchain_file()
    
        components = self.create_components(peer_names, gblock, blockchain,
                                       private_key, port)
        server_controller = components[0]
        state_machine = components[1]
    
        # Create the emulator for sleeping and waking up
        gossip_handler = GossipLayer(private_key, params, block_limit,
                            server_controller.request_handler,
                            state_machine.handshake_handler.request_creator)
    
    
        # Start the protocol runner
        self.spin_server_forever(gossip_handler, server_controller, state_machine)
    
    
    
    def create_components(self, peer_names, gblock, blockchain,
                          private_key, port):
        """
           Create all the components needed for a successful emulation.
           :param peer_names: A list of strings.
           :param gblock: A GenesisBlock object.
           :param blockchain: A Blockchain object.
           :param private_key: An _EllipticCurvePrivateKey for signing blocks.
           :param port: An integer.
        """
        # Create the Vector clock to be used by the request handler and emulator.
        vector_clock = VectorClock(self.userid, peer_names, gblock.hash(), blockchain) 
    
        # Create server and client.
        server = VegvisirServer(port, self.userid)
        client = VegvisirClient(self.userid)
    
        # Create the network operator.
        network = EmulationNetworkOperator(server, client)
    
        # Create the request handlers and creators.
        request_handler = PeerRequestHandler(blockchain, network, vector_clock, self.protocol)
        request_creator = ProtocolRequestCreator(blockchain, network, private_key)
    
    
        # Create the sendall protocol server.
        sendall_server = SendallServer(request_handler, self.crash_prob)
    
        # Create the frontier protocol client and server.
        frontier_client =  FrontierClient(request_creator, request_handler,
                                          self.crash_prob)
    
        frontier_server = FrontierServer(request_creator, request_handler,
                                         self.crash_prob)
    
        # Create the handshake handler for the handshake protocol.
        handshake_handler = HandshakeHandler(frontier_server)
    
        # Create the frontier message handler.
        frontier_handler = FrontierHandler(private_key, frontier_server,
                                           frontier_client)
    
        # Create the vector server to be used by the server_controllerl.
        vector_server = VectorServer(request_creator, request_handler, vector_clock,
                                     self.crash_prob)
    
        # Create the state machine for processing messages.
        state_machine = StateMachine(network, frontier_handler, frontier_server,
                                     sendall_server, vector_server,
                                     handshake_handler) 
    
        server_controller = ServerController(sendall_server, frontier_server, 
                                             request_handler, vector_server)
    
        return server_controller, state_machine
        
    
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
        gblock, my_private_key = self.recreate_genesis(genesis)
    
        # Copy keystore
        keystore = vegvisir.Blockchain.Keystore()
        keystore.CopyFrom(blockchain.keystore)
        ks = self.recreate_keystore(keystore)
    
        # Recreate the blockchain
        fset_crdt = Crdt()
        blockchain = Blockchain(gblock, ks, fset_crdt)
        
        # Add the genesis block as the start frontier hash
        blockchain.crdt.addset.add(gblock.hash())
    
        return gblock, blockchain, my_private_key 
    
    
    def spin_server_forever(self, gossip_handler, controller, state_machine):
        """
           Listen for new connections and process new data.
           :param gossip_handler: A GossipLayer object.
           :param controller: A ServerController object.
           :param state_machine: A StateMachine object.
        """
        network = controller.request_handler.network
        server = network.server
        server.bind_server()
    
        # Initialize inputs and outputs for selector
        network.inputs.append(network.server.server_socket)
        error_statuses = [comstatus.SOCKET_ERROR, comstatus.NO_DATA]
    
        while network.inputs:
            readable, writable, exceptional = select(network.inputs,
                                                     network.outputs,
                                                     network.inputs, 1)
            for incoming in readable:
                print("Readables %s\n" % readable)
                if incoming is server.server_socket:
                    print("INCOMING AT SERVER SOCKET %s\n" % server.userid)
                    connection, client_address = incoming.accept()
                    print("%s : New connection with peer %s at port %s\n" \
                          % (server.userid, client_address[0], 
                                  client_address[1]))
                    connection.settimeout(SOCKET_TIMEOUT)
                    network.add_connection(connection, client_address)
                    print("Adding %s to inputs\n" % connection)
                else:
                    payload = network.receive(incoming) 
                    print("Payload \n %s\n" % payload)
                    if payload in error_statuses:
                        state_machine.destroy_session(incoming)
                        if incoming in network.outputs:
                            network.outputs.remove(incoming)
                    else:
                        if not incoming in network.outputs:
                            network.outputs.append(incoming)
                        state_machine.process_message(payload, incoming)
                        print("Processing message is done %s\n" % server.userid)
            for ready in writable:
                if ready in network.message_queues:
                    try:
                        outgoing_msg = network.message_queues[ready].get_nowait()
                    except queue_is_empty:
                        print("No message in %s's queue atm" % ready)
                        network.outputs.remove(ready)
                    else:
                        status = network.send(outgoing_msg, ready)
                        print("Sending status %s\n" % status)
                elif ready in network.outputs:
                     network.outputs.remove(ready)
            for exception in exceptional:
                print("Connection to %s ran into an exception\n" % exception.getpeername())
                if exception in network.outputs:
                    network.outputs.remove(exception)
                state_machine.destroy_session(exception)
    
            # Gossip only if we have not initiated another gossip round.
            if len(network.outgoing_connections) < 1:
                print("No outgoing connections yet..\n")
                gossip_handler.random_sleep()
                if gossip_handler.block_limit > 0:
                    gossip_handler.generate_new_block()
                gossip_status, connection = gossip_handler.initiate_gossip()
                if gossip_status == rstate.HANDSHAKE:
                    print("%s GOSSIP STARTED SUCCESSFULLY!\n" % server.userid)
                    state_machine.update_state(connection, rstate.HANDSHAKE)
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
    
