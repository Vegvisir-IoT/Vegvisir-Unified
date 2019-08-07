import unittest
import inspect
from time import time

from vegvisir.blockchain.block import Transaction, Block
from vegvisir.blockchain.blockdag import GenesisBlock, Blockchain
from vegvisir.blockchain.authentication import Certificate, Keystore
from vegvisir.blockchain.crdt import Crdt
from vegvisir.blockchain.crypto import (generate_private_key,
                                        generate_public_key,
                                        generate_keypair, verify_signature,
                                        serialize_public_key)
from vegvisir.simulator.opcodes import Operation, Event

from vegvisir.tests import factories

''' Initial test for the package.
    TODO: Establish a way to ensure that the protobuff package is
    configured correctly.'''

def generate_ca_certificate():
    """ Generate a CA certificate for different test cases. """
    ca_private_key, ca_public_key = generate_keypair()
    ca_cert = factories.CertificateFactory(public_key=ca_public_key)
    ca_cert.sign(ca_private_key)

    return ca_cert, ca_private_key


def generate_user_certificates(num, ca_private_key):
    """
        Generate a list of user certificates for a blockchain.
        :param num: an int.
        :param ca_private_key: _EllipticCurvePrivateKey
        :rtype: a list of Certificates
    """
    user_certs = []
    user_private_keys = []
    for _ in range(num):
        private_key, public_key = generate_keypair()
        cert = factories.CertificateFactory(public_key=public_key)
        cert.sign(ca_private_key)
        user_certs.append(cert)
        user_private_keys.append(private_key)

    return user_certs, user_private_keys


def generate_blockchain():
    """ Generate a blockchain.
        :rtype: a list.
    """

    ca_cert, ca_private_key = generate_ca_certificate()
    user_certs, private_keys = generate_user_certificates(10, ca_private_key)

    # Create keystore.
    keystore = factories.KeystoreFactory(ca_cert=ca_cert)

    # Create Crdt.
    crdt = factories.CrdtFactory()

    # Create genesis block.
    gblock = factories.GenesisBlockFactory(ca_cert=ca_cert,
                                           certlist=user_certs)
    gblock.sign(ca_private_key)

    chain = factories.BlockchainFactory(genesis_block=gblock,
                                        keystore=keystore,
                                        crdt=crdt)

    return user_certs, private_keys, chain


def generate_and_add_blocklist(user_id, user_private_key, chain, num):
    """ Generate a number of blocks and add them to a user's blockchain.
        :param user_id: a String.
        :param user_private_key: _EllipticCurvePrivateKey.
        :param chain: a Blockchain.
        :param num: an int.
        :rtype: a list of bytestrings.
    """

    previous_block_hash = None
    block_hashes = []
    block_map = {}

    for i in range(num):
        if i == 0:
            genesis_hash = chain.genesis_block.hash()
            block = factories.BlockFactory(userid=user_id,
                                           parents=[genesis_hash])
            block.sign(user_private_key)
            current_bhash = block.hash()
            block_hashes.append(current_bhash)
            previous_block_hash = current_bhash
            block_map[current_bhash] = block
        else:
            block = factories.BlockFactory(userid=user_id,
                                           parents=[previous_block_hash])
            block.sign(user_private_key)
            current_bhash = block.hash()
            block_hashes.append(current_bhash)
            previous_block_hash = current_bhash
            block_map[current_bhash] = block

    chain.add_list(block_hashes, block_map)

    return block_hashes


# @brief: A class for testing model classes.
class TestModelClasses(unittest.TestCase):

    def test_certificate(self):
        """ Test the Certificate constructor and methods. """

        print("Test started: %s" % inspect.stack()[0][3])
        private_key, public_key = generate_keypair()
        cert = factories.CertificateFactory(public_key=public_key)
        self.assertIsInstance(cert, Certificate)

        # Test serialization
        partial_cert_bytestring = cert.serialize_partial()
        self.assertIsInstance(partial_cert_bytestring, bytes)

        # Test deserialization
        deserialized_cert = cert.get_public_key()
        self.assertFalse(isinstance(deserialized_cert, bytes))

        # Test signing
        cert.sign(private_key)
        self.assertIsInstance(cert.sig, bytes)
        full_cert_bytestring = cert.serialize()
        self.assertIsNotNone(full_cert_bytestring)

        # Test verification
        _, bogus_public_key = generate_keypair()
        verification = cert.verify(bogus_public_key)
        self.assertFalse(verification)
        print("Test passed: %s\n" % inspect.stack()[0][3])

    def test_keystore(self):
        """ Test the Keystore contructor and methods. """

        print("Test started: %s" % inspect.stack()[0][3])
        ca_private_key, ca_public_key = generate_keypair()
        ca_cert = factories.CertificateFactory(public_key=ca_public_key)
        ca_cert.sign(ca_private_key)

        keystore = factories.KeystoreFactory(ca_cert=ca_cert)
        self.assertIsInstance(keystore, Keystore)

        _, user1_pub_key = generate_keypair()
        user_cert1 = factories.CertificateFactory(public_key=user1_pub_key)
        user_cert1.sign(ca_private_key)
        keystore.add_certificate(user_cert1)

        # Test user membership
        ca_user_id = ca_cert.userid
        self.assertTrue(keystore.has_user(ca_user_id))

        # Test key serialization
        ca_public_key_copy = keystore.get_public_key(ca_user_id)
        ca_key_hash = serialize_public_key(ca_public_key)
        ca_copy_key_hash = serialize_public_key(ca_public_key_copy)
        self.assertEqual(ca_key_hash, ca_copy_key_hash)

        # Test bogus user membership
        bogus_userid = "Balegamire"
        user1_id = user_cert1.userid
        current_users = keystore.get_users()
        self.assertIn(user1_id, current_users)
        self.assertNotIn(bogus_userid, current_users)

        print("Test passed: %s\n" % inspect.stack()[0][3])

    def test_transaction(self):
        """ Test the Transaction constructor and methods. """

        print("Test started: %s" % inspect.stack()[0][3])
        tx1 = factories.TransactionFactory()
        self.assertIsInstance(tx1, Transaction)
        self.assertLess(tx1.timestamp, time())

        # Test serialization
        data = tx1.serialize_partial()
        self.assertIsInstance(data, bytes)
        print("Test passed: %s\n" % inspect.stack()[0][3])

    def test_block(self):
        """ Test the block constructor and methods. """

        print("Test started: %s" % inspect.stack()[0][3])
        block = factories.BlockFactory()
        self.assertIsInstance(block, Block)
        private_key = generate_private_key()
        public_key = generate_public_key(private_key)

        # Sign the block.
        block.sign(private_key)

        # Test block serialization.
        data = block.serialize_partial()

        # Verify the validity of the block.
        self.assertIsNotNone(block.sig)
        self.assertTrue(verify_signature(data, block.sig, public_key))
        print("Test passed: %s\n" % inspect.stack()[0][3])

    def test_genesis_block(self):
        """ Test the genesis block constructor and methods. """

        print("Test started: %s" % inspect.stack()[0][3])
        ca_cert, ca_private_key = generate_ca_certificate()

        # Create a list of user certificates
        user_certs, _ = generate_user_certificates(5, ca_private_key)

        gblock = factories.GenesisBlockFactory(ca_cert=ca_cert,
                                               certlist=user_certs)
        self.assertIsInstance(gblock, GenesisBlock)

        # Test signing and hashing
        gblock.sign(ca_private_key)
        self.assertIsInstance(gblock.sig, bytes)
        self.assertIsInstance(gblock.hash(), bytes)
        print("Test passed: %s\n" % inspect.stack()[0][3])

    def test_blockchain(self):
        """ Test the blockchain constructor. """

        print("Test started: %s" % inspect.stack()[0][3])
        # Create CA certificate.
        ca_cert, ca_private_key = generate_ca_certificate()

        # Create user certificates.
        user_certs, _ = generate_user_certificates(10, ca_private_key)

        # Create keystore.
        keystore = factories.KeystoreFactory(ca_cert=ca_cert)

        # Create Crdt.
        crdt = factories.CrdtFactory()

        # Create genesis block.
        gblock = factories.GenesisBlockFactory(ca_cert=ca_cert,
                                               certlist=user_certs)
        gblock.sign(ca_private_key)

        chain = factories.BlockchainFactory(genesis_block=gblock,
                                            keystore=keystore,
                                            crdt=crdt)
        self.assertIsInstance(chain, Blockchain)
        print("Test passed: %s\n" % inspect.stack()[0][3])

    def test_crdt(self):
        """ Test the crdt constructor and methods. """

        print("Test started: %s" % inspect.stack()[0][3])
        crdt = factories.CrdtFactory()
        self.assertIsInstance(crdt, Crdt)

        # Generate and sign a block
        private_key, _ = generate_keypair()
        block = factories.BlockFactory()
        block.sign(private_key)

        # Test that block is added
        crdt.add_new_block(block, Operation.PROVIDED_POW)
        self.assertIn(block.hash(), crdt.frontier_set())
        print("Test passed: %s\n" % inspect.stack()[0][3])


# @brief: A class for testing critical blockchain methods.
class TestMethods(unittest.TestCase):

    def test_blockchain_basic_add(self):
        """ Test the blockchain add method. """

        print("Test started: %s" % inspect.stack()[0][3])
        user_certs, private_keys, chain = generate_blockchain()

        # Test legitimate block
        user1_id = user_certs[0].userid
        user1_private_key = private_keys[0]
        block1 = factories.BlockFactory(userid=user1_id,
                                        parents=chain.frontier_nodes)
        block1.sign(user1_private_key)
        self.assertIsInstance(block1.sig, bytes)
        self.assertTrue(chain.add(block1, Event.RECORD_REQUEST))
        self.assertIn(block1.hash(), chain.blocks)
        self.assertIn(block1.hash(), chain.frontier_nodes)

        # Test illegitimate block
        block2 = factories.BlockFactory(userid=user1_id,
                                        parents=[block1.hash()])
        block2.sign(user1_private_key)
        block3 = factories.BlockFactory(userid=user1_id,
                                        parents=[block2.hash()])
        block3.sign(user1_private_key)
        self.assertFalse(chain.add(block3, Event.RECORD_REQUEST))
        print("Test passed: %s\n" % inspect.stack()[0][3])

    def test_blockchain_add_list(self):
        """ Test the blockchain add_list method. """

        print("Test started: %s" % inspect.stack()[0][3])
        user_certs, private_keys, chain = generate_blockchain()

        # Test adding a list of blocks
        user_id = user_certs[0].userid
        user_private_key = private_keys[0]

        block_hashes = generate_and_add_blocklist(user_id,
                                                  user_private_key,
                                                  chain, 4)

        for b_hash in block_hashes:
            self.assertIn(b_hash, chain.blocks)

        self.assertEqual(set([block_hashes[3]]), chain.frontier_nodes)

        print("Test passed: %s\n" % inspect.stack()[0][3])

    def test_blockchain_topological_sort(self):
        """ Test blockchain order when merging two blockchains. """

        print("Test started: %s" % inspect.stack()[0][3])
        user_certs, private_keys, chain = generate_blockchain()

        user1_id = user_certs[0].userid
        user1_private_key = private_keys[0]

        generate_and_add_blocklist(user1_id, user1_private_key, chain, 100)

        ordered_chain = chain.get_topological_sort()
        self.assertIsInstance(ordered_chain[0], GenesisBlock)
        print("Test passed: %s\n" % inspect.stack()[0][3])



if __name__ == '__main__':
    unittest.main()
