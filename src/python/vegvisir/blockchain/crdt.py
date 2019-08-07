#!/usr/bin/python3

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza, Pablo Fiori"]


# @brief A class for 2P set operations
class Crdt(object):
    """
        Implements the device state as a CRDT dealing with frontier nodes
        "A" set is an add-only set containing blocks that were recently
        added to the blockchain.
        "R" set is another add-only set containing the parent hashes of
        blocks that were recently added to the blockchain.
        "A-R" is an easy way to compute the frontier nodes on the blockchain.
        The frontier set, along with the latest_operation, encapsulate state.
    """
    def __init__(self):
        self.addset = set()
        self.removeset = set()
        self.latest_operation = None

    def add_new_block(self, block, operation):
        """
            Adds a new block hash to the 2P set.

            :param block: a Block object.
            :param operation: an Enum.
        """
        self.addset.add(block.hash())
        for b_hash in block.parents:
            self.removeset.add(b_hash)
        self.latest_operation = operation

    def frontier_set(self):
        """ Return the frontier set. """

        return self.addset.difference(self.removeset)
