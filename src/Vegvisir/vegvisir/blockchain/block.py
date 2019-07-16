from .crypto import sign, verify_signature, hash_data
from .blockchain_helpers import (int_to_bytestring, double_to_bytestring,
                                 str_to_bytestring)


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza, Kolbeinn Karlsson, Pablo Fiori"]


# @brief A class for handling block transactions.
class Transaction(object):

    def __init__(self, userid, timestamp, tx_dict):
        """ Create a Transaction from a dictionary. 

            :param userid: a string.
            :param timestamp: a string representation of time in milliseconds.
            :param tx_dict: a dictionary of the record id and comment.
        """

        self.userid = userid
        self.timestamp = timestamp
        self.recordid = tx_dict['recordid']
        self.comment = tx_dict['comment']

    def serialize_partial(self):
        """ Serialize the transaction data. """

        bytestring = int_to_bytestring(self.userid)
        bytestring += double_to_bytestring(self.timestamp)
        bytestring += int_to_bytestring(self.recordid)
        bytestring += self.comment
        return bytestring

    def hash(self):
        """ Hash the serialized transaction data. """
        data = self.serialize_partial()
        return hash_data(data)

    def print_tx(self):
        tx_string = "{ Userid: " + self.userid + "\n"
        tx_string += "Timestamp: " + str(self.timestamp) + "\n"
        tx_string += "Recorid: " + str(self.recordid) + "\n"
        tx_string += "Comment: " + str(self.comment) + " }\n"
        return tx_string


# @brief A class representing a single block in the blockchain.
class Block(object):

    def __init__(self, userid, timestamp, parents, tx, location=None):
        """
            Construct a new block object.
            :param userid: a string.
            :param timestamp: a string representation of time in milliseconds.
            :param tx: a list of one or more Transaction objects.
            :param location: a string.
        """
        self.userid = userid
        self.timestamp = timestamp
        self.location = location
        self.parents = parents
        self.tx = tx
        self.sig = None
        # Block objects also keep track of their children. This is
        # not a true part of the block and should not be included
        # in the serialization
        self.children = []
        # Partial_data avoids multiple calls to partial serialization
        # when signing, hashing, and verifying the block data.
        self.partial_data = self.serialize_partial()

    def serialize_partial(self):
        try:
            bytestring = str_to_bytestring(self.userid)
            bytestring += double_to_bytestring(self.timestamp)
            for parent in self.parents:
                bytestring += parent
            for tx in self.tx:
                bytestring += int_to_bytestring(tx.recordid)
                bytestring += tx.comment
        except Exception as e:
            print("Error serializing block -> %s\n" % e)
        return bytestring

    def hash(self):
        """ Compute and return the block hash. """
        bytestring = self.partial_data + self.sig
        return hash_data(bytestring)

    def sign(self, private_key):
        self.sig = sign(self.partial_data, private_key)

    def verify(self, public_key):
        data = self.partial_data
        return verify_signature(data, self.sig, public_key)

    def print_block(self):
        block_string = "Block Userid: " + self.userid + "\n"
        block_string += "Block Timestamp: " + str(self.timestamp) + "\n"
        block_string += "Block Parents: " + str(self.parents) + "\n"
        for tx in self.tx:
            block_string += tx.print_tx()
        block_string += "Signature: " + str(self.sig) + "\n"
        print(block_string)
