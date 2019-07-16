from random import randint, uniform
from time import time

import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.emulator.socket_opcodes import ProtocolState as state
from vegvisir.protocols.protocol import Protocol


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

 
# @brief: A class that handles the vegvisir protocol by sending all blocks.
class SendallServer(Protocol):

    """
        A data structure representing a device.

        :param request_handler: A ProtocolRequestHandler for creating requests.
        :param crash_prob: An float.
    """
    def __init__(self, request_handler, crash_prob):
        Protocol.__init__(self, request_handler.userid,
                          request_handler.blockchain, crash_prob)
        self.userid = request_handler.userid
        self.request_handler = request_handler
        self.network = request_handler.network
        self.reconciliations = [] 


    def process_all_blocks(self, message):
        """
            Receive all blocks from remote peer.
            :param message: A SendallMessage protobuf object.
        """
        start_time = time()

        # Emulate probability of crash in the middle of reconcilation.
        self.emulate_crash_probability()

        # Check if peer sent a request to add all their blocks
        nstate = self.request_handler.handle_add_block_request(message)
        end_time = time() - start_time
        recon_data = {'recon_time': end_time}

        print("----- %s SENDALL RECONCILIATION STATS -----\n\n" %

              self.userid)
        print("Total rec time %s secs" % recon_data['recon_time'])
        self.reconciliations.append(recon_data)
        return state.EVEN


    def print_reconciliation_stats(self):
        """ Print the simulated reconciliation times. """

        print("----- %s SERVER RECONCILIATION RESULTS -----\n\n", self.userid)
        all_recs = len(self.reconciliations)
        total_rec = 0
        for rec in self.reconciliations:
            total_rec += rec['recon_time']                        
        if all_recs > 0:
            avg_rec_time = float(total_rec / all_recs)
        print("Average rec time %s\n" % avg_rec_time)

