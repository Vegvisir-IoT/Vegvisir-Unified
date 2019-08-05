from time import time
from crypto import *
from vegvisir import *
import unittest
import inspect
import random
import string
import pickle

#####################################
#   Helper Classes and Methods      #
#   for Unit testing                #
#####################################

def generate_genesis_block(cert, certlist=[]):
    # Generate public-private keypair
    private_key = generate_private_key()
    public_key = generate_public_key(private_key)
    userid = 1
    cert = Certificate(userid, public_key)
    cert.sign(private_key)

    # Generate Genesis Block
    userid = 1
    timestamp = 1520352090.1310000
    gblock = GenesisBlock(userid, timestamp, cert, certlist)
    gblock.sign(private_key)
    return gblock

def generate_transaction():
    tx_dict = {'recordid': 2918, 'comment': b'A Comment.'}
    timestamp = 1520352090.1317804
    userid = 93468713
    tx = Transaction(userid, timestamp, tx_dict)
    return tx

class Blockfactory(object):

    def __init__(self, userid, private_key):
        self.userid = userid
        self.private_key = private_key

    def generate_comment(self, length):
        chars = string.ascii_uppercase + string.ascii_lowercase + string.digits
        comment = ''.join(random.choice(chars) for _ in range(length))
        return comment.encode()

    def newblock(self, parents):
        recordid = random.randint(1, 1000000)
        comment_length = random.randint(10, 60)
        comment = self.generate_comment(comment_length)
        tx = {'recordid': recordid, 'comment': comment}
        block = Block(self.userid, time(), parents, tx)
        block.sign(self.private_key)
        return block


#################################
#   Test Package                #
#################################
class Test_Block(unittest.TestCase):
    def test_block_constructor(self):
        print("Test started: %s" %inspect.stack()[0][3])
        userid = 7356
        timestamp = 1519916383.9118667
        parent_1 = hash(b'parent1')
        parent_2 = hash(b'parent2')
        parents = [parent_1, parent_2]
        recordid = 98312
        comment = b'This is a comment'
        tx = {'recordid': recordid, 'comment': comment}
        private_key = generate_private_key()
        public_key = generate_public_key(private_key)
        block = Block(userid, timestamp, parents, tx)
        block.sign(private_key)
        data = block.serialize_partial()

        assert verify_signature(data, block.sig, public_key)
        assert block.userid == userid
        assert block.timestamp == timestamp
        assert block.parents == parents
        assert block.tx == tx
        assert block.sig is not None
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_blockchain_basic(self):
        print("Test started: %s" %inspect.stack()[0][3])
        private_key, public_key = generate_keypair()
        userid = 1
        cert = Certificate(userid, public_key)
        cert.sign(private_key)
        ks = Keystore(cert)
        gblock = generate_genesis_block(cert)
        csm = CrdtStateMachine()
        chain = Blockchain(gblock, csm, ks)
        assert chain.blocks[gblock.hash()] == gblock
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_blockchain_add(self):
        print("Test started: %s" %inspect.stack()[0][3])
        csm = CrdtStateMachine()
        ca_private_key, ca_public_key = generate_keypair()
        ca_userid = 1
        cert = Certificate(ca_userid, ca_public_key)
        cert.sign(ca_private_key)
        ks = Keystore(cert)

        user_private_key, user_public_key = generate_keypair()
        userid = 352
        user_cert = Certificate(userid, user_public_key)
        user_cert.sign(ca_private_key)

        gblock = generate_genesis_block(cert, [user_cert])
        chain = Blockchain(gblock, csm, ks)

        block = Block(userid, 1520352090.1317802,
                      [gblock.hash()], {'recordid': 21, 'comment': b''})
        block.sign(user_private_key)
        good_block_added = chain.add(block)

        private_key, public_key = generate_keypair()
        block2 = Block(353, 1520352090.1317900, [
                       hash(b'bungalow')], {'recordid': 20, 'comment': b' '})
        block2.sign(private_key)
        bad_block_added = chain.add(block2)

        block3 = Block(353, 1520352090.1317900,
                       [gblock.hash()], {'recordid': 20, 'comment': b' '})
        block3.sign(private_key)
        unauthorized_block_added = chain.add(block3)

        assert good_block_added
        assert bad_block_added == False
        assert unauthorized_block_added == False
        assert chain.blocks[block.hash()] == block
        assert block2.hash() not in chain.blocks.keys()
        print("Test passed: %s" %inspect.stack()[0][3])



    def test_transaction_basic(self):
        print("Test started: %s" %inspect.stack()[0][3])
        tx = generate_transaction()
        assert tx is not None

        hash1 = tx.hash()
        hash2 = tx.hash()

        assert hash1 is not None
        assert hash1 == hash2
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_CrdtStateMachine_basic(self):
        print("Test started: %s" %inspect.stack()[0][3])
        csm = CrdtStateMachine()
        tx1 = generate_transaction()
        tx2 = generate_transaction()
        tx2.userid = 387625190
        tx2.timestamp += 0.001

        csm.add_tx(tx1)
        csm.add_tx(tx2)

        assert tx1.hash() in csm.txmap.keys()
        assert tx2.hash() in csm.txmap.keys()
        assert any([x.powi for x in csm.txmap.values()]) == False
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_Certificate_selfsigned(self):
        print("Test started: %s" %inspect.stack()[0][3])
        userid = 1
        private_key, public_key = generate_keypair()
        cert = Certificate(userid, public_key)
        cert.sign(private_key)
        assert cert.verify(public_key)
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_Keystore_constructor(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Create a keystore
        userid = 1
        private_key, public_key = generate_keypair()
        cert = Certificate(userid, public_key)
        cert.sign(private_key)
        ks = Keystore(cert)

        assert ks.has_user(1)
        assert not ks.has_user(23)

        public_key2 = ks.get_public_key(1)
        pks1 = serialize_public_key(public_key)
        pks2 = serialize_public_key(public_key2)

        assert pks1 == pks2
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_Keystore_add_certificate(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Create a keystore
        ca_userid = 1
        ca_private_key, ca_public_key = generate_keypair()
        ca_cert = Certificate(ca_userid, ca_public_key)
        ca_cert.sign(ca_private_key)
        ks = Keystore(ca_cert)

        userid = 7
        private_key, public_key = generate_keypair()
        cert = Certificate(userid, public_key)
        cert.sign(ca_private_key)

        ks.add_certificate(cert)

        assert ks.has_user(userid)

        private_key2, public_key2 = generate_keypair()
        userid2 = 18
        cert2 = Certificate(userid2, public_key2)
        cert2.sign(private_key)

        ks.add_certificate(cert2)

        assert not ks.has_user(userid2)
        print("Test passed: %s" %inspect.stack()[0][3])



    def test_blockchain_children(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Generate self-signed certificate
        private_key, public_key = generate_keypair()
        userid = 1
        cert = Certificate(userid, public_key)
        cert.sign(private_key)
        # Create keystore
        ks = Keystore(cert)
        # Create genesis block
        gblock = GenesisBlock(userid, time(), cert, [])
        gblock.sign(private_key)
        # Create CRDT State Machine
        csm = CrdtStateMachine()
        # Create blockchain
        chain = Blockchain(gblock, csm, ks)

        # Create a number of blocks
        bf = Blockfactory(userid, private_key)
        block1 = bf.newblock([gblock.hash()])
        block2 = bf.newblock([gblock.hash()])
        block3 = bf.newblock([block2.hash()])
        block4 = bf.newblock([block1.hash(), block3.hash()])
        # Add them to the blockchain
        chain.add(block1)
        chain.add(block2)
        chain.add(block3)
        chain.add(block4)

        # Now walk through the blockchain
        assert set(gblock.children) == set([block1.hash(), block2.hash()])
        assert set(block1.children) == set([block4.hash()])
        assert set(block2.children) == set([block3.hash()])
        assert not block4.children
        print("Test passed: %s" %inspect.stack()[0][3])

    def test_blockchain_topological_sort(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Generate self-signed certificate
        private_key, public_key = generate_keypair()
        userid = 1
        cert = Certificate(userid, public_key)
        cert.sign(private_key)
        # Create keystore
        ks = Keystore(cert)
        # Create genesis block
        gblock = GenesisBlock(userid, time(), cert, [])
        gblock.sign(private_key)
        # Create CRDT State Machine
        csm = CrdtStateMachine()
        # Create blockchain
        chain = Blockchain(gblock, csm, ks)

        # Create a number of blocks
        bf = Blockfactory(userid, private_key)
        block1 = bf.newblock([gblock.hash()])
        block2 = bf.newblock([gblock.hash()])
        block3 = bf.newblock([block2.hash()])
        block4 = bf.newblock([block1.hash(), block3.hash()])
        # Add them to the blockchain
        chain.add(block1)
        chain.add(block2)
        chain.add(block3)
        chain.add(block4)

        tsort = chain.get_topological_sort()

        assert len(tsort) == 5

        # Now walk through the list and make sure they preserve the topological order
        counted = set()
        assert isinstance(tsort[0], GenesisBlock)
        counted.add(tsort[0].hash())
        for i in range(1, len(tsort)):
            for parent in tsort[i].parents:
                assert parent in counted
            counted.add(tsort[i].hash())
        print("Test passed: %s" %inspect.stack()[0][3])

    def test_blockchain_merge(self):
        print("Test started: %s" %inspect.stack()[0][3])
        # Generate self-signed certificate
        private_key, public_key = generate_keypair()
        userid = 1
        cert = Certificate(userid, public_key)
        cert.sign(private_key)
        # Create keystore
        ks = Keystore(cert)
        # Create genesis block
        gblock = GenesisBlock(userid, time(), cert, [])
        gblock.sign(private_key)
        # Create CRDT State Machine
        csm = CrdtStateMachine()
        # Create blockchain
        chain = Blockchain(gblock, csm, ks)

        # Create a number of blocks
        bf = Blockfactory(userid, private_key)
        block1 = bf.newblock([gblock.hash()])
        block2 = bf.newblock([gblock.hash()])
        block3 = bf.newblock([block2.hash()])
        block4 = bf.newblock([block1.hash(), block3.hash()])
        # Add them to the blockchain
        chain.add(block1)
        chain.add(block2)
        chain.add(block3)
        #chain.add(block4)

        # Create second blockchain
        csm2 = CrdtStateMachine()
        ks2 = Keystore(cert)
        chain2 = Blockchain(gblock, csm2, ks2)
        chain2.add(block1)
        chain2.add(block2)
        chain2.add(block3)
        chain2.add(block4)

        # Merge them together
        chain.merge(chain2.get_topological_sort())

        # See if merge happened correctly
        assert block4.hash() in chain.blocks
        # TODO  more rigorous testing
        print("Test passed: %s" %inspect.stack()[0][3])


    def test_pickle_blockchain(self):
        print("Test started: %s" %inspect.stack()[0][3])
        """ I want to be able to pickle a blockchain. """
        # Generate self-signed certificate
        private_key, public_key = generate_keypair()
        userid = 1
        ca_cert = Certificate(userid, public_key)
        ca_cert.sign(private_key)
        # Generate user certificate
        user_private_key, user_public_key = generate_keypair()
        user_userid = 3
        user_cert = Certificate(user_userid, user_public_key)
        user_cert.sign(private_key)
        # Create keystore
        ks = Keystore(ca_cert)
        # Create genesis block
        gblock = GenesisBlock(userid, time(), ca_cert, [user_cert])
        gblock.sign(private_key)
        # Create CRDT State Machine
        csm = CrdtStateMachine()
        # Create blockchain
        chain = Blockchain(gblock, csm, ks)
        # Create a number of blocks
        bf = Blockfactory(userid, private_key)
        block1 = bf.newblock([gblock.hash()])
        block2 = bf.newblock([gblock.hash()])
        block3 = bf.newblock([block2.hash()])
        block4 = bf.newblock([block1.hash(), block3.hash()])
        block5 = Blockfactory(user_userid, user_private_key).newblock([block4.hash()])
        # Add them to the blockchain
        chain.add(block1)
        chain.add(block2)
        chain.add(block3)
        chain.add(block4)
        chain.add(block5)

        # Get the topological sort
        tsort = chain.get_topological_sort()

        # Now pickle the blockchain
        data = pickle.dumps(tsort)
        print("Test passed: %s" %inspect.stack()[0][3])



if __name__ == '__main__':
    unittest.main()

