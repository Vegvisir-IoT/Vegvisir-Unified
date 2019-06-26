#!/usr/bin/python3 


import sys


from vegvisir.blockchain.crypto import (generate_private_key, generate_keypair,
                                 generate_public_key)
from vegvisir.blockchain.authentication import Certificate, Keystore
from vegvisir.blockchain.blockdag import GenesisBlock, Blockchain
from vegvisir.blockchain.crdt import Crdt
from vegvisir.emulator.emulation_helpers import protobufy_certificate 
from vegvisir.emulator.emulation_helpers import parse_chain_creator_args
from vegvisir.protos import charlottewrapper_pb2 as block_wrapper

# The default peer address
HOST = '127.0.0.1'


# @brief A class that generates emulator components for experiments.
class BlockchainGenerator(object):

    """
        A data structure for emulating the Vegvisir protocol.

        :param copies: An integer, the number of blockchain copies to make.
    """
    def __init__(self,chainfile, paramfile, 
                 user_names=["Alpha", "Bravo", "Charlie"], peer_map=None):
        self.user_names = user_names 
        self.peer_map = peer_map
        self.chainfile = chainfile
        self.paramfile = paramfile


    def create_components(self):
        """
            Create components for each node including certificates, keystore
            protocol controllers, etc.
        """
        # Generate public and private keys for the certificate authority.
        ca_private_key = generate_private_key()
        ca_public_key = generate_public_key(ca_private_key)

        # Create an (arbitrarily selected) user id.
        userid = 1
    
        # Create a new certificate for the certificate authority.
        ca_cert = Certificate(userid, ca_public_key)

        # Certificate authority signs off on their own new certificate.
        ca_cert.sign(ca_private_key)
        ks = Keystore(ca_cert)

        self.users = self.generate_blockchain(ca_private_key, ca_cert, ks)

    
    def generate_blockchain(self, ca_private_key, ca_cert, ks):
        """ Generate the blockchain and write it to a file.

            :param ca_private_key: An _EllipticCurvePrivateKey.
            :param ca_cert: A Certificate.
            :param ks: A Keystore.
        """
        cert_list = []
        key_list = []
        user_list = []
        if not self.peer_map:
            self.peer_map = [{'hostname': HOST, 'port' : 9001},
                             {'hostname': HOST, 'port' : 9002}, 
                             {'hostname': HOST, 'port' : 9003}]

        for idx in range(len(self.user_names)):
            # Generate a public and private key for the user certificate.
            private_key, public_key = generate_keypair()
            userid = self.user_names[idx]

            # Create a certificate for a user.
            cert = Certificate(userid, public_key, private_key=private_key)

            # Certificate authority has to sign off on new user certificate.
            cert.sign(ca_private_key)
            cert_list.append(cert)
            key_list.append(private_key)

        # Generate the genesis block with a list of all certifications.
        gblock = GenesisBlock(1, 0.000, ca_cert, cert_list)

        # Certificate authority signs off on the Genesis block.
        gblock.sign(ca_private_key)
        crdt = Crdt()
        chain = Blockchain(gblock, ks, crdt)

        # Serialize the Blockchain and save it to a file
        chain_copy = block_wrapper.Blockchain()

        # Copy ca certificate info
        ca_cert_copy = protobufy_certificate(ca_cert, ca_cert=True)

        # Copy genesis block info
        genesis_copy = block_wrapper.Block.GenesisBlock()
        genesis_copy.userid = str(gblock.userid)
        genesis_copy.timestamp = gblock.timestamp
        genesis_copy.ca_certificate.CopyFrom(ca_cert_copy)
        for cert in gblock.certlist:
            serialized_cert = protobufy_certificate(cert)
            cert_copy = genesis_copy.certlist.add()
            cert_copy.CopyFrom(serialized_cert)
        genesis_copy.signature = gblock.sig

        # Copy keystore info
        ks_copy = block_wrapper.Blockchain.Keystore()
        ks_copy.ca_cert.CopyFrom(ca_cert_copy)
        for usr_idx in range(len(self.user_names)):
            userid = self.user_names[usr_idx]
            userid_copy = ks_copy.userids.add()
            userid_copy.userid = userid
            serialized_cert_copy = protobufy_certificate(ks.certs[userid])
            cert_copy = ks_copy.usercerts.add()
            cert_copy.CopyFrom(serialized_cert_copy)

        # Copy all contents to blockchain
        chain_copy.genesis.CopyFrom(genesis_copy)
        chain_copy.keystore.CopyFrom(ks_copy)
            
        # Serialize and save the blockchain
        fd = open(self.chainfile, "wb")
        fd.write(chain_copy.SerializeToString())
        fd.close()

        # Create a list of parameters for each user and save them to a file
        fd = open(self.paramfile, "w")
        for usr_idx in range(len(self.user_names)):
            user_params = self.user_names[usr_idx] + ","
            my_hostname = str( self.peer_map[usr_idx]['hostname']) 
            my_port = self.peer_map[usr_idx]['port']
            user_params += str(my_hostname) + "," + str(my_port) + ","
            peers = [x for x in self.peer_map if x['port'] != my_port]
            for peer_idx in range(len(peers)):
                if peer_idx < (len(peers) - 1):
                    user_params += str(peers[peer_idx]['hostname']) + ","
                    user_params += str(peers[peer_idx]['port']) + ","
                else:
                    user_params += str(peers[peer_idx]['hostname']) + ","
                    user_params += str(peers[peer_idx]['port'])
            user_params += "\n"
            fd.write(user_params)
        fd.close()
        return user_list


if __name__ == '__main__':

    # Generate the blockchain and user parameters.
    if sys.argv[1] == 'test':
        peers = [{'hostname': HOST, 'port': '9000'}, {'hostname': HOST, 'port': '9001'}]
        usernames = ['Alice', 'Bob']
        chainfile = 'test_chain.txt'
        paramfile = 'test_param.txt'
        generator = BlockchainGenerator(chainfile, paramfile,
                                        usernames, peers)
    else:
        args = parse_chain_creator_args()
        generator = BlockchainGenerator(args['chainfile'], args['paramfile'],
                                        args['usernames'], args['peers'])
    generator.create_components()

