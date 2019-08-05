from random import choice, randint, SystemRandom
from string import ascii_letters, digits
from time import time
from argparse import ArgumentParser


from vegvisir.blockchain.block import Transaction, Block
from vegvisir.blockchain.authentication import Certificate
from vegvisir.blockchain.crypto import (deserialize_public_key,
                                        deserialize_private_key)
from vegvisir.blockchain.blockchain_helpers import str_to_bytestring
from vegvisir.proto import vegvisir_pb2 as vegvisir


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

# A module for generating/transforming transactions, blocks, and blockchains.

def create_random_tx(userid, size):
    """
        Generate a random transaction.
        :param userid: A string
        :param size: An int. 
        :rtype: Transaction.
    """
    record_id = randint(1, pow(2,12))
    all_chars = ascii_letters + digits
    comment = ''.join(SystemRandom().choice(all_chars) for _ in range(size))
    comment = str_to_bytestring(comment)
    tx_dict = {'recordid' : record_id, 'comment' : comment}
    new_tx = Transaction(userid, time(), tx_dict)
    return new_tx

def create_blocks(userid, parents, private_key, num_blocks):
    """
        Generate blocks for node with random transactions.
        :param userid: A string.
        :param parents: A list of hashes.
        :param private_key: An _EllipticCurvePrivateKey for signing blocks.
        :param num_blocks: An integer.
        :rtype: Block
    """
    block_list = []
    start_time = time()  
    updated_parents = parents
    for index in range(num_blocks):
         tx_list = [create_random_tx(userid, 6)]
         # Add location to block later if possible.
         block = Block(userid, time(), updated_parents, tx_list)
         block.sign(private_key)
         block_list.append(block)
         updated_parents = [block.hash()]
    end_time = time() - start_time
    print("Block generation took %s secs!\n" % end_time)
    return block_list

def protobufy_certificate(cert, ca_cert=False):
    """
        Generate a protobuf version of a user certificate.
        :param cert: A Certificate in Blockchain.authentication.Ceritificate.
        :param ca_cert: boolean.
        :rtype: A Certificate in protos.charlottewrapper.
    """
    cert_copy = vegvisir.Certificate()
    cert_copy.userid = str(cert.userid)
    cert_copy.public_key = cert.public_key
    if not ca_cert:
        cert_copy.private_key = cert.private_key
    cert_copy.signature = cert.sig
    return cert_copy

def deserialize_certificate(cert, ca_cert=False, genesis=False):
    """
        Deserialize a certificate read from file.
        :param cert: A Certificate in charlottewrapper.
        :param ca_cert: A boolean. Determine whether to deserialize the
                        private key. The ca_cert does not save its own
                        because it is used to sign other certificates.
                        We might need to change this later when we start
                        adding blocks that contain new certificates added
                        to the protocol by the CA authorizing new users.
        :param genesis: A boolean. Determine whether to cast the userid into
                        an int.
        :rtype: A Certificate in Blockchain.authentication.Ceritificate
    """
    signature = cert.signature

    # Don't deserialize a ca_cert private key
    if not (ca_cert or genesis):
        userid = cert.userid
        private_key = deserialize_private_key(cert.private_key)
    else:
        userid = int(cert.userid)
        private_key = None

    public_key = deserialize_public_key(cert.public_key)
    cert_copy = Certificate(userid, public_key, private_key)
    cert_copy.sig = signature
    return cert_copy, private_key


def update_local_vector_maps(vector_clock, block):
    """
       Update the vector maps whenever new blocks are created and whenever
       new blocks are sent by a peer.
       :param vector_clock: A VectorClock object.
       :param block: A Block object.
    """
    # Find the list of parents in the vector clock for this block
    userids, blocks = vector_clock.get_all_leading_blocks()

    # Add the block mapping
    old_leader = vector_clock.get_leading_block(block.userid)
    if vector_clock.offline_activity == True:
        vector_clock.add_block_mapping(block.userid, old_leader + 1, block.hash(),
                                       [block.userid], [old_leader]) 
    else:
        vector_clock.add_block_mapping(block.userid, old_leader + 1, block.hash(),
                                       userids, blocks)

    # Register the block as a child of its parents.
    vector_clock.update_vector(block.userid, old_leader + 1, block.hash()) 
    new_leader = vector_clock.get_leading_block(block.userid)
    block_tag = str(block.userid) + str(new_leader)
    for parent_hash in block.parents:
        parent_tag = vector_clock.get_block_tag(parent_hash) 
        vector_clock.add_block_children(parent_tag, parent_hash, block_tag)

def parse_main_args():
    """ Parse the arguments passed by the user. """

    protocol_message = ("The flag for whether the protocol will be run \
                         locally or remotely")
    
    username_message = ("The username of the node for which to \
                              start the protocol")

    chain_message = ("The name of the file containing the essential parts \
                      for recreating the blockchain")

    params_message = ("The name of the file containing the parameters for \
                       other nodes such as their port numbers and hostnames")
    crash_message = ("The probability that the node will crash in range \
                      0-1")
    block_message = ("The number of blocks to create at once")
    protocol_list_message = ("The protocol that the node is capable \
                             of speaking")

    parser = ArgumentParser(description='Retrieve arguments for a vegvisir emulation')

    parser.add_argument('-r', '--run', type=str, help=protocol_message,
                        choices=['local', 'remote'], required=True)

    parser.add_argument('-u', '--username', type=str,
                        help=username_message, required=True)
    parser.add_argument('-c', '--chainfile', type=str,
                        help=chain_message, required=True)
    parser.add_argument('-p', '--paramfile', type=str,
                        help=params_message, required=True)
    parser.add_argument('-d', '--deathprob', type=float,
                        help=crash_message, default=0.00)
    parser.add_argument('-b', '--blocklimit', type=int,
                        help=block_message, default=0)
    parser.add_argument('-l', '--protocol', type=str,
                         help=protocol_list_message,
                         choices=['vector', 'frontier', 'sendall'],
                         required=True)

    args = parser.parse_args()

    # Assign to variables
    run = args.run
    username = args.username
    cfile = args.chainfile
    params = args.paramfile

    crash_prob = args.deathprob
    if crash_prob < 0 or crash_prob >= 1:
        print("Crash probability has to be between 0 and 1 exclusive!\n")
        exit(1)


    block_limit = args.blocklimit

    protocol = args.protocol
    protocol = protocol.upper()
    print("Protocol %s\n" % protocol)

    args = {'run': run, 'username': username, 'chainfile': cfile,
            'paramsfile': params, 'crash_prob': crash_prob,
            'block_limit': block_limit, 'protocol': protocol}
    return args

 
def parse_chain_creator_args():
    """ Parse the arguments for the central controller. """

    username_message = ("The list of usernames for each peer address provided")
    peer_message = ("[Peer address and respective ports] in csv form")
    param_file_message = ("The file that will contain the parameters for peer addresses")
    chain_message = ("The file that will contain the genesis block and keystore")

    parser = ArgumentParser(description='Provide arguments for the protocol run')

    parser.add_argument('-pe', '--peers', 
                         help=peer_message, nargs='+', required=True)
    parser.add_argument('-u', '--usernames', type=str, help=username_message,
			nargs='+', required=True)
    parser.add_argument('-pf', '--paramfile', type=str, 
                        help=param_file_message, required=True)
    parser.add_argument('-c', '--chainfile', type=str,
                         help=chain_message, required=True)

    args = parser.parse_args()

    # Assign variables and return
    usernames = args.usernames[0].split(",")
    peers = args.peers[0].split(",")
    paramfile = args.paramfile
    chainfile = args.chainfile

    # Perform checks on userinput
    if len(usernames) != (len(peers)/2):
        print("Please enter a IP,PORT pair for each username!\n")
        exit(1)
    if len(peers) % 2 != 0:
        print("Please enter an equal number of peers and ports!\n")
        exit(1)
    peer_map = []
    for i in range(len(peers)):
        if i % 2 == 0:
            peer_map.append({'hostname': peers[i], 'port': peers[i+1]})
            i += 2	
    args = {'usernames': usernames, 'peers': peer_map, 'paramfile': paramfile,
            'chainfile': chainfile}
    return args
