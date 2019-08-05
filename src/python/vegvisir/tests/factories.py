#!/usr/bin/python3

from string import ascii_letters, digits
from random import choice, randint, SystemRandom
from time import time

import factory

from vegvisir.blockchain import block, blockdag, authentication, crdt
from vegvisir.blockchain.blockchain_helpers import str_to_bytestring

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

def generate_random_id():
    """ Generates a random number for a user/record id. """

    return randint(1, pow(2,32))

def generate_timestamp():
    """ Generates a timestamp for a transaciton based on current time. """
    return time()

def generate_tx_dict(size):
    """ 
        Generates a random transaction for testing purposes.
        :param size: the size of the comment string to be generated.
        :rtype: dictionary.
    """
    record_id = generate_random_id()
    all_chars = ascii_letters + digits
    comment = ''.join(SystemRandom().choice(all_chars) for _ in range(size))
    comment = str_to_bytestring(comment)
    tx_dict = {'recordid' : record_id, 'comment' : comment}
    return tx_dict

class TransactionFactory(factory.Factory):
    class Meta:
        model = block.Transaction
    
    userid = generate_random_id()
    timestamp = factory.LazyFunction(generate_timestamp)
    tx_dict = generate_tx_dict(6)

class CertificateFactory(factory.Factory):
    class Meta:
        model = authentication.Certificate
    
    userid = factory.Faker('name')


class KeystoreFactory(factory.Factory):
    class Meta:
        model = authentication.Keystore


class BlockFactory(factory.Factory):
    class Meta:
        model = block.Block
    
    userid = factory.Faker('name')
    timestamp = factory.LazyFunction(generate_timestamp)
    parents = []
    tx = TransactionFactory.create_batch(5)


class GenesisBlockFactory(factory.Factory):
    class Meta:
        model = blockdag.GenesisBlock
    
    userid = generate_random_id()
    timestamp = factory.LazyFunction(generate_timestamp)

class CrdtFactory(factory.Factory):
    class Meta:
        model = crdt.Crdt
        
class BlockchainFactory(factory.Factory):
    class Meta:
        model = blockdag.Blockchain
