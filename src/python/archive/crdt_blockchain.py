#   crdt_blockchain.py
#   Creation to handle Conflict Free Replicated Data Type BlockChains
#   Holds Class and Utility methods for handling, sorting, and creating
#   crdts. This is a light-weight version of package where encryption and
#   network communication not utilized.

# from crypto import *
import time as t
import random

class GenesisBlock():
    def __init__(self, userid, timestamp, ca_cert, certlist):
        self.userid         = userid
        self.timestamp      = timestamp
        self.ca_cert        = ca_cert
        self.certlist       = certlist
        self.children       = []    # 
        self.mystery        = 0

    def hash( self ):
        self.mystery = random.randint(0, 5728342)
        return self.mystery

class Blockchain():
    def __init__(self, genesis_block):
        self.blocks = {genesis_block.hash() : genesis_block}
        self.genesis_block      = genesis_block
        self.k_requirement      = 3
        self.mem_cache          = []        #List Transaction objects


    def display_mem_cache( self ):
        msg = ""
        for transaction in self.mem_cache:
            msg += "Issued by: {}    Comment: {}\n".format(
                    transaction.user_id, transaction.comment)
            msg +="Time: {}    PoWi: {}\nWitnesses: {}\n".format(
                    transaction.timestamp, transaction.PoWi,
                    transaction.witnesses)
        return msg

    def hash( self ):
        taken = list( self.blocks.keys() )
        notFound = True
        temp = random.randint(0, 5728342)
        return temp

    def print_chain( self ):
        msg ="Blocks: {}\nGenesis: {}\n".format(self.blocks,self.genesis_block)
        msg += "Req: {} witnesses\nAwaiting Transactions: {}".format(
                self.k_requirement, self.display_mem_cache())
        print(msg)

    def add(self, block):
        ## Guard Checks Here
        #   1. parents on chain
        #   2. timestamps
        #   3. Hash block add to block chain
        new_hash = self.hash()
        for p in block.parents:
            self.blocks[p].children.append(new_hash)
        self.blocks[new_hash] = block
        return True

    def adjust_k(self, num):
        self.k_requirement = num

    def evaluate_mem_cache( self, signature ):
        update_list = []
        for x, tx in enumerate(self.mem_cache):
            if len( tx.witnesses ) < self.k_requirement:
                if signature == tx.user_id or signature in tx.witnesses:
                    update_list.append( ("SEND", x) )
                else:
                    update_list.append( ("SIGN", x) )
            else:
                is_verified = tx.at_threshhold( self.k_requirement )
                if is_verified:
                    update_list.append("PROCESS", x)
                else:
                    print("FAILURE NOT ENOUGH Signatuares")
        return update_list


class Block():
    def __init__(self, userid, timestamp, parents, tx, req):
        self.userid             = userid
        self.timestamp          = timestamp
        self.parents            = parents
        self.children           = []
        self.tx                 = tx
        self.sig                = None
        self.witness_list       = []
        self.w_requirement      = req


    def store_tx(self, tx):
        self.tx = Transaction(self.userid, self.timestamp,
                tx['recordid'], tx['comment'])
    def sign(self, data, private_key):
        self.sig = str(userid) + " " + str(timestamp)


##  Transaction
class Transaction:
    def __init__(self, userid, timestamp, dictionary):
        self.user_id                = userid
        self.PoWi                   = False
        self.timestamp              = timestamp
        self.record_id              = dictionary['recordid']
        self.comment                = dictionary['comment']
        self.witnesses              = set()

    def verify_transaction( self, user_id ):
        if user_id == self.user_id or user_id in self.witnesses:
            return False
        else:
            self.witnesses.add( user_id )
            return True

    def at_threshhold( self, number):
        if len(witnesses) >= number:
            self.PoWi   = True
            return True
        else:
            return False

    def compare_transaction(self, new_tx):
        if self.user_id == new_tx.user_id and self.record_id == new_tx.record_id:
            return True
        else:
            return False
