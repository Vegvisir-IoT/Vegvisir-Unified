from random import randint, uniform
from time import time

import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.emulator.socket_opcodes import (ProtocolStatus as ps,
                                              CommunicationStatus as comstatus)
from vegvisir.protocols.protocol import Protocol

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

 
# @brief: A class that handles the vegvisir protocol by sending all blocks.
class SendallClient(Protocol):

    """
        A data structure representing a device.

        :param request_creator: A ProtocolRequestCreator for creating requests.
        :param crash_prob: An float.
    """
    def __init__(self, request_creator, crash_prob):
        Protocol.__init__(self, request_creator.userid, request_creator.blockchain, crash_prob)
        self.request_creator = request_creator
        self.network = request_creator.network
        self.reconciliations = [] 


    def sendallblocks(self):
        """
            Send all blocks to remote peer.
        """
        start_time = time()

        total_blocks = len(self.blockchain.blocks.keys()) - 1

        # Create a request for the peer to add all of our blocks
        sendall_request = self.request_creator.add_all_blocks_request()
        status = self.network.send(sendall_request, request_type="ADD BLOCK")
        if status == comstatus.SOCKET_ERROR:
            return ps.RECONCILIATION_FAILURE
 
        end_request = self.request_creator.end_protocol_request()

        # Emulate probability of crash in the middle of reconcilation.
        self.emulate_crash_probability()

        status = self.network.send(end_request, request_type="END PROTOCOL")
        if status == comstatus.SOCKET_ERROR:
            return ps.RECONCILIATION_FAILURE

        end_time = time() - start_time
        recon_data = {'total_blocks': total_blocks, 
                      'recon_time': end_time}

        print("----- %s CLIENT SENDALL RECONCILIATION RESULTS -----\n\n" %

              self.userid)
        print("%s Client, Blocks sent %s, Total rec time %s secs" % (
                   self.userid, recon_data['total_blocks'],
                    recon_data['recon_time']))
        self.reconciliations.append(recon_data)
        return ps.SUCCESS

    def print_reconciliation_stats(self):
        """ Print the simulated reconciliation times. """

        print("----- %s CLIENT RECONCILIATION RESULTS -----\n\n", self.userid)
        all_recs = len(self.reconciliations)
        total_rec = 0
        for rec in self.reconciliations:
            total_rec += rec['recon_time']                        
        if all_recs > 0:
            avg_rec_time = float(total_rec / all_recs)
        print("Average rec time %s\n" % avg_rec_time)

