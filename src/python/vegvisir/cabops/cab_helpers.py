from random import randint

from ..blockchain.block import Transaction
from ..blockchain.blockchain_helpers import str_to_bytestring

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

def create_transactions(cab_id, cab_fd, num_tx):
    """
        Creates transactions for a given cab. 
        
        @param cab_id, the cab's id (e.g.  new_abboip)
        @param cab_fd: a file descriptor for the cab.
        @param num_tx: the number of transactions to be created. 
    """
    location = None
    incomplete = False
    transactions = []
    for i in range(num_tx):
        line = cab_fd.readline()
        if line:
            comment = str_to_bytestring(line)
            record_id = randint(1,4294967296 )
            tx_dict = {'recordid': record_id, 'comment' : comment}
            line = line.split()
            new_tx = Transaction(cab_id, int(line[3]), tx_dict)
            transactions.append(new_tx)
            if i == 0:
                location = {'id': cab_id, 'latitude': float(line[0]), 'longitude' : float(line[1])}
        else:
            incomplete = True
            print("The read didn't return transactions at index %s\n" % i)
            break
    return {'tx': transactions, 'loc': location, 'inc': incomplete}
