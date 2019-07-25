
from .blockchain_helpers import (int_to_bytestring, double_to_bytestring,
                                 str_to_bytestring)
from .crypto import sign, hash_data
from vegvisir.simulator.opcodes import Operation
from vegvisir.design_pattern.observer import Observable


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza, Kolbeinn Karlsson, Pablo Fiori"]


#   @brief An class representing the genesis block in the blockchain.
class GenesisBlock(object):

    def __init__(self, userid, timestamp, ca_cert, certlist):
        """
            Create the genesis block.

            :param userid: string.
            :param timestamp: double.
            :param ca_cert: a Certificate object, self-signed.
            :param certlist: a list of Certificate objects.
        """
        self.userid = userid
        self.timestamp = timestamp
        self.ca_cert = ca_cert
        self.certlist = certlist
        self.children = []  # Just for record keeping

    def serialize_partial(self):
        """ Serialize GenesisBlock data. """

        bytestring = int_to_bytestring(self.userid)
        bytestring += double_to_bytestring(self.timestamp)
        bytestring += self.ca_cert.serialize()
        for cert in self.certlist:
            bytestring += cert.serialize()
        return bytestring

    def sign(self, private_key):
        """ Certify data with a user's private key. """

        data = self.serialize_partial()
        self.sig = sign(data, private_key)

    def hash(self):
        bytestring = self.serialize_partial()
        bytestring += self.sig
        return hash_data(bytestring)


# @brief A class representing the block dag.
class Blockchain(Observable):

    def __init__(self, genesis_block, keystore, crdt):
        """
            Create the block chain.

            :param genesis_block: GenesisBlock.
            :param keystore: Keystore.
            :param crdt: Crdt.
        """
        Observable.__init__(self)
        self.blocks = {genesis_block.hash(): genesis_block}
        for cert in genesis_block.certlist:
            keystore.add_certificate(cert)
        self.keystore = keystore
        self.genesis_block = genesis_block
        self.frontier_nodes = set([genesis_block.hash()])
        self.crdt = crdt

    def add_list(self, hashes, block_map):
        """
            Add a list of blocks from a reconciliation to the blockchain.
            The list of hashes already ordered so that parents of a block
            will be added before the block itself.

            :param hashes: a list of hashes.
            :param block_map: a dictionary of hash => Block.
        """
        for h in hashes:
            block = block_map[h]
            self.add(block, Operation.RECONCILED_PEER)

    def add(self, block, operation):
        """
            Add a single block to the blockchain.

            :param block: Block.
            :param operation: an option from the Operation Enum.
            :rtype: Boolean.
        """

        # Check if parents on chain
        parents_on_chain = all([key in self.blocks for key in block.parents])
        if not parents_on_chain:
            return False

        # Check if timestamp is greater than parents'
        p_timestamps = [self.blocks[p].timestamp for p in block.parents]
        timestamp_greater = max(p_timestamps) < block.timestamp
        if not timestamp_greater:
            print("Block timestamp is not greater than parents' timestamps")
            print("Block p timestamps -> %s" % p_timestamps)
            print("Block timestamp -> %s\n" % block.timestamp)
            return False

        if not self.keystore.has_user(block.userid):
            print("The keystore does not have the userid\n")
            return False

        # Verify signature
        public_key = self.keystore.get_public_key(block.userid)
        block.verify(public_key)

        # All checks have passed. Add block to blockchain
        self.blocks[block.hash()] = block
        self.crdt.add_new_block(block, operation)

        # Record that the block is a child of its parents
        for p in block.parents:
            self.blocks[p].children.append(block.hash())

        # Update frontier set
        self.frontier_nodes = self.crdt.frontier_set()

        # Update all the observers for the blockchain
        self.set_changed()
        self.notify_observers(block)

        return True

    def _topological_sort_dfs(self, block, blocklist):
        if blocklist.count(block) > 0:
            return
        for child_hash in block.children:
            child = self.blocks[child_hash]
            self._topological_sort_dfs(child, blocklist)
        blocklist.append(block)

    def get_topological_sort(self):
        """ Returns a list of blocks in the blockchain
        that preserves their topological order."""
        blocks = []
        self._topological_sort_dfs(self.genesis_block, blocks)
        blocks.reverse()
        return blocks

    def merge(self, tsort):
        for block in tsort:
            if block.hash() not in self.blocks:
                added = self.add(block, Operation.RECONCILED_PEER)
                if not added:
                    return False
        return True
