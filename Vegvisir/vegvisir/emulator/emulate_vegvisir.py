from select import select


from vegvisir.blockchain.blockdag import GenesisBlock, Blockchain
from vegvisir.blockchain.authentication import Certificate, Keystore
from vegvisir.blockchain.crdt import Crdt
from vegvisir.emulator.server import VegvisirServer
from vegvisir.emulator.client import VegvisirClient
from vegvisir.emulator.serverside_controller import ServerController
from vegvisir.emulator.clientside_controller import ClientController
from vegvisir.emulator.peer_request_handlers import PeerRequestHandler
from vegvisir.emulator.protocol_request_handler import ProtocolRequestCreator
from vegvisir.emulator.emulator import Emulator
from vegvisir.emulator.emulation_helpers import deserialize_certificate
from vegvisir.emulator.socket_opcodes import ProtocolStatus as ps
from vegvisir.network.emulation_network import EmulationNetworkOperator
from vegvisir.protos import charlottewrapper_pb2 as block_wrapper
from vegvisir.protocols.vector.vector import VectorClock
from vegvisir.protocols.vector.vectorserver import VectorServer
from vegvisir.protocols.sendall.sendallclient import SendallClient
from vegvisir.protocols.sendall.sendallserver import SendallServer
from vegvisir.protocols.frontier.frontierclient import FrontierClient
from vegvisir.protocols.frontier.frontierserver import FrontierServer


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


SOCKET_TIMEOUT = 2 


# @brief A module for emulating the vegvisir protocol on each node

def emulate_vegvisir(args):
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

    port, peer_names, params = read_parameters(collective_params, my_userid)

    gblock, blockchain, private_key = read_blockchain_file(genesis_block_file,
                                                      my_userid)

    client_controller, server_controller = create_components(my_userid,
                                                            peer_names,
                                                            gblock,
                                                            blockchain,
                                                            private_key,
                                                            port,
                                                            crash_prob,
                                                            protocol)

    # Create the emulator for sleeping and waking up
    emulator = Emulator(params, client_controller, block_limit)

    # Start the protocol runner
    spin_server_forever(emulator, server_controller)



def create_components(userid, peer_names, gblock, blockchain,
                      private_key, port, crash_prob, protocol):
    """
       Create all the components needed for a successful emulation.
       :param userid: A string.
       :param peer_names: A list of strings.
       :param gblock: A GenesisBlock object.
       :param blockchain: A Blockchain object.
       :param private_key: An _EllipticCurvePrivateKey for signing blocks.
       :param port: An integer.
       :param crash_prob: A float.
       :param protocol: A string.
    """
    # Create the Vector clock to be used by the request handler and emulator.
    vector_clock = VectorClock(userid, peer_names, gblock.hash()) 

    # Create server and client.
    server = VegvisirServer(port, userid)
    client = VegvisirClient(userid)

    # Create the network operator.
    network = EmulationNetworkOperator(server, client)

    # Create the request handlers and creators.
    request_handler = PeerRequestHandler(blockchain, network, vector_clock, protocol)
    request_creator = ProtocolRequestCreator(blockchain, network)

    # Create the sendall protocol client and server.
    sendall_client = SendallClient(request_creator, crash_prob)

    sendall_server = SendallServer(request_handler, crash_prob)

    # Create the frontier protocol client and server.
    frontier_client =  FrontierClient(private_key, request_handler,
                                      request_creator, crash_prob)

    frontier_server = FrontierServer(private_key, request_handler,
                                     request_creator, crash_prob)

    # Create the vector server to be used by the server_controllerl.
    vector_server = VectorServer(request_creator, vector_clock,
                                 crash_prob)

    # Create server and client controllers.
    client_controller = ClientController(sendall_client, frontier_client,
                                         request_handler, request_creator)

    server_controller = ServerController(sendall_server, frontier_server, 
                                         request_handler, vector_server)

    return client_controller, server_controller
    

def read_parameters(paramfile, userid):
    """
       :param paramfile: A string.
       :param userid: A string.
    """
    fd = open(paramfile, "r")
    found_user = False
    peer_names = []
    line = fd.readline()
    while line:
        params = line.split(",")
        peer_names.append(params[0])
        if params[0] == userid:
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
        print("%s user not found!\n" % userid)
        exit(1)

    print("Username %s found!\n" % userid)
    return my_port, peer_names, peers
 

def read_blockchain_file(chainfile, userid):
    """
       :param chainfile: A string.
       :param userid: A string.
    """
    
    # Read main blockchain parameters from file
    fd = open(chainfile, "rb")
    blockchain = block_wrapper.Blockchain()
    blockchain.ParseFromString(fd.read())

    # Copy genesis parameters and private_key
    genesis = block_wrapper.Block.GenesisBlock()
    genesis.CopyFrom(blockchain.genesis)
    gblock, my_private_key = recreate_genesis(genesis, userid)

    # Copy keystore
    keystore = block_wrapper.Blockchain.Keystore()
    keystore.CopyFrom(blockchain.keystore)
    ks = recreate_keystore(keystore)

    # Recreate the blockchain
    fset_crdt = Crdt()
    blockchain = Blockchain(gblock, ks, fset_crdt)
    
    # Add the genesis block as the start frontier hash
    blockchain.crdt.addset.add(gblock.hash())

    return gblock, blockchain, my_private_key 


def spin_server_forever(emulator, controller):
    """
       Listen for new connections and process new data.
       :param emulator: An Emulator object.
       :param controller: A ServerController object.
    """
    network = controller.request_handler.network
    server = network.server
    server.bind_server()

    # Initialize inputs and outputs for selector
    network.inputs.append(network.server.server_socket)
    outputs = []

    while network.inputs:
        readable, _, exceptional = select(network.inputs, outputs, network.inputs, 1)
        print("Readables %s\n" % readable)
        client_socket = network.client.client_socket
        for incoming in readable:
            if incoming is server.server_socket:
                print("INCOMING AT SERVER SOCKET %s\n" % server.userid)
                connection, client_address = incoming.accept()
                print("%s : New connection with peer %s at port %s\n" \
                      % (server.userid, client_address[0], 
                              client_address[1]))
                connection.settimeout(SOCKET_TIMEOUT)
                network.add_connection(connection, client_address)
                print("Added %s to inputs\n" % connection)
            elif incoming is client_socket:
                # We are receiving from a connection initiated by the client.
                print("INCOMING DATA FROM %s's client conn" % server.userid)
                protoc_status = controller.process_incoming_payload(incoming,
                                                                 network.inputs)
                if protoc_status == ps.SUCCESS:
                    print("PROTOCOL RUN WAS SUCCESSFUL AT %s SERVER\n" %
                          server.userid)
                elif protoc_status == ps.CONNECTION_FAILURE:
                    print("%s, PROTOCOL FAILED, CLIENT CLOSED CONNECTION\n" %
                          server.userid)
                    network.remove_connection(incoming) 
                elif protoc_status == ps.ONGOING_VECTOR_PROTOCOL:
                    print("VECTOR PROTOCOL STILL GOING -> %s\n" % 
                           server.userid)
                else:
                    print("PROTOCOL FAILED ON %s SERVER, STATUS -> %s\n" %
                         (server.userid, protoc_status))
            else:
                print("INCOMING DATA TO %s SERVER\n" % server.userid)
                print("Client socket %s\n" % client_socket)
                protoc_status = controller.process_incoming_payload(incoming,
                                                                network.inputs)
                if protoc_status == ps.SUCCESS:
                    print("PROTOCOL RUN WAS SUCCESSFUL AT %s\
                       SERVER\n" % server.userid)
                    network.remove_connection(incoming)
                elif protoc_status == ps.CONNECTION_FAILURE:
                    print("%s, PROTOCOL FAILED, CLIENT CLOSED CONNECTION\n" %
                          server.userid)
                    network.remove_connection(incoming) 
                elif protoc_status == ps.ONGOING_VECTOR_PROTOCOL:
                    print("VECTOR PROTOCOL STILL GOING at %s\n" % 
                           server.userid)
                else:
                    print("PROTOCOL FAILED ON %s SERVER, STATUS -> %s\n" %
                         (server.userid, protoc_status))
                    network.remove_connection(incoming)
        for exception in exceptional:
            print("Connection to %s ran into an exception\n" % exception.getpeername())
            network.remove_connection(exception)

        # Gossip only if we have not initiated another gossip round.
        #if not(client_socket in network.inputs):
        if len(network.inputs) == 1:
            if emulator.block_limit > 0:
                emulator.generate_new_block()
               # emulator.random_sleep()
            gossip_status = emulator.wake_up_to_gossip()
            print("%s Gossip status -> %s\n" % (server.userid,
                                                gossip_status))
            if gossip_status == ps.SUCCESS:
                print("%s GOSSIPED SUCCESSFULLY!\n" % server.userid)
            elif gossip_status == ps.ONGOING_VECTOR_PROTOCOL:
               print("%s LEFT GOSSIPING RND UNFINISHED\n" % server.userid)
            elif gossip_status == ps.PROTOCOL_DISAGREEMENT:
              print("%s COULD NOT AGREE ON PROTOCOL W/ PEER\n" %
                    server.userid)
            else:
                print("%s GOSSIPING FAILED, STATUS %s, INPUT\n" %
                      (server.userid, gossip_status))
        else:
            print("INPUTS %s\n" % network.inputs)

def recreate_genesis(genesis, username):
    """
        Recreate the genesis block from a block read in from a file.
        :param genesis: A protos.charlottewrapper.Blockchain.GenesisBlock.
        :param username: A string.
        :rtype: A list of a GenesisBlock and a private_key.
    """
    gen_userid = int(genesis.userid)
    gen_timestamp = genesis.timestamp
    ca_cert, _ = deserialize_certificate(genesis.ca_certificate, ca_cert=True,
                                      genesis=True)
    cert_list = []
    for cert in genesis.certlist:
        deserialized_cert, potential_key = deserialize_certificate(cert)
        if deserialized_cert.userid == username:
            my_private_key = potential_key
        cert_list.append(deserialized_cert)

    # Create genesis_block
    gblock = GenesisBlock(gen_userid, gen_timestamp, ca_cert, cert_list)
    gblock.sig = genesis.signature
    return gblock, my_private_key


def recreate_keystore(keystore):
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

