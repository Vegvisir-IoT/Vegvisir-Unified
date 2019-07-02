from select import select
from queue import Empty as queue_is_empty


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
from vegvisir.emulator.stateless.state_machine import StateMachine
from vegvisir.emulator.socket_opcodes import (ProtocolStatus as ps,
                                             CommunicationStatus as comstatus)
from vegvisir.network.emulation_network import EmulationNetworkOperator
from vegvisir.proto import vegvisir_pb2 as vegvisir 
from vegvisir.protocols.vector.vector import VectorClock
from vegvisir.protocols.vector.vectorserver import VectorServer
from vegvisir.protocols.sendall.sendallclient import SendallClient
from vegvisir.protocols.sendall.sendallserver import SendallServer
from vegvisir.protocols.frontier.stateless_frontier_client import FrontierClient
from vegvisir.protocols.frontier.stateless_frontier_server import FrontierServer
from vegvisir.protocols.frontier.frontier_handler import FrontierHandler

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

    components = create_components(my_userid, peer_names, gblock, blockchain,
                                   private_key, port, crash_prob,protocol)
    client_controller = components[0]
    server_controller = components[1]
    state_machine = components[2]

    # Create the emulator for sleeping and waking up
    emulator = Emulator(private_key, params, client_controller, block_limit)


    # Start the protocol runner
    spin_server_forever(emulator, server_controller, state_machine)



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
    frontier_client =  FrontierClient(request_creator, request_handler,
                                      crash_prob)

    frontier_server = FrontierServer(request_creator, request_handler,
                                     crash_prob)

    # Create the frontier message handler.
    frontier_handler = FrontierHandler(private_key, frontier_server,
                                       frontier_client, request_handler)

    # Create the vector server to be used by the server_controllerl.
    vector_server = VectorServer(request_creator, vector_clock,
                                 crash_prob)

    # Create the state machine for processing messages.
    state_machine = StateMachine(network, frontier_handler, frontier_server,
                                 vector_server) 

    # Create server and client controllers.
    client_controller = ClientController(sendall_client, frontier_client,
                                         request_handler, request_creator)

    server_controller = ServerController(sendall_server, frontier_server, 
                                         request_handler, vector_server)

    return client_controller, server_controller, state_machine
    

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
    blockchain = vegvisir.Blockchain()
    blockchain.ParseFromString(fd.read())

    # Copy genesis parameters and private_key
    genesis = vegvisir.Block.GenesisBlock()
    genesis.CopyFrom(blockchain.genesis)
    gblock, my_private_key = recreate_genesis(genesis, userid)

    # Copy keystore
    keystore = vegvisir.Blockchain.Keystore()
    keystore.CopyFrom(blockchain.keystore)
    ks = recreate_keystore(keystore)

    # Recreate the blockchain
    fset_crdt = Crdt()
    blockchain = Blockchain(gblock, ks, fset_crdt)
    
    # Add the genesis block as the start frontier hash
    blockchain.crdt.addset.add(gblock.hash())

    return gblock, blockchain, my_private_key 


def spin_server_forever(emulator, controller, state_machine):
    """
       Listen for new connections and process new data.
       :param emulator: An Emulator object.
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
                print("Added %s to inputs\n" % connection)
            else:
                payload = network.receive(incoming) 
                print("Payload is %s\n" % payload)
                if payload in error_statuses:
                    state_machine.destroy_session(incoming)
                    if connection in network.outputs:
                        network.outputs.remove(connection)
                else:
                    if not connection in network.outputs:
                        network.outputs.append(connection)
                    state_machine.process_message(payload, connection)
        for ready in writable:
            try:
                outgoing_message = network.message_queues[ready].get_nowait()
            except queue_is_empty:
                print("No message in %s's queue atm" % ready)
                network.outputs.remove(ready)
            else:
                status = network.send(outgoing_message, ready)
                print("Sending status %s\n" % status)
        for exception in exceptional:
            print("Connection to %s ran into an exception\n" % exception.getpeername())
            if exception in network.outputs:
                network.outputs.remove(exception)
            state_machine.destroy_session(exception)

        # Gossip only if we have not initiated another gossip round.
        if len(network.outgoing_connections) < 1:
            emulator.random_sleep()
            if emulator.block_limit > 0:
                emulator.generate_new_block()
            gossip_status = emulator.wake_up_to_gossip()
            print("%s Gossip status -> %s\n" % (server.userid,
                                                gossip_status))
            if gossip_status == ps.SUCCESS:
                print("%s GOSSIP STARTED SUCCESSFULLY!\n" % server.userid)
            else:
                print("%s GOSSIPING FAILED, STATUS %s, INPUT\n" %
                      (server.userid, gossip_status))


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

