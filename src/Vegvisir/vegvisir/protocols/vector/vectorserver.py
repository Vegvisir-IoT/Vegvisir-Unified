from time import time


import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.emulator.socket_opcodes import ProtocolState as rstate
from vegvisir.protocols.protocol import Protocol as p
from vegvisir.protocols.vector.vector import VectorClock 
from vegvisir.protocols.protocol import Protocol


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

 
# @brief: A class that handles the vegvisir protocol using vector clocks.
class VectorServer(Protocol):

    """
        A data structure representing a device.

        :param request_creator: A ProtocolRequestCreator for creating requests.
        :param vector_clock: A VectorClock object.
        :param crash_prob: A float.
    """
    def __init__(self, request_creator, request_handler, vector_clock,
                 crash_prob):
        Protocol.__init__(self, request_creator.userid, request_creator.blockchain,
                          crash_prob)
        self.request_creator = request_creator
        self.request_handler = request_handler
        self.network = request_creator.network
        self.reconciliations = [] 
        self.vector_clock = vector_clock
        self.reconciliations = []


    def handle_peer_update(self, all_clocks, state):
        """
            Respond to a peer's update based on their clock.
            :param all_clocks: a vgp.VectorClock protobuf object.
            :param state: A dictionary. 
        """
        # Emulate probability of crash in the middle of reconciliation.
        self.emulate_crash_probability()

        missing_hashes = self.vector_clock.compute_dependencies(all_clocks)
        print("MISSING HASHES %s\n" % missing_hashes)
        if missing_hashes == "Invalid vector clock":
            return rstate.PROTOCOL_DISAGREEMENT
        if missing_hashes != None:
            missing_blocks = []
            for block_hash in missing_hashes:
                missing_blocks.append(self.blockchain.blocks[block_hash])
            request = self.request_creator.add_blocks_request("vector",
                                                       blocks=missing_blocks,
                                                       end_protocol=True)
            state['message_queue'].put(request)
            return rstate.LOCAL_DOMINATES 
        return rstate.EVEN
  

    def process_blocks(self, message):
        """
            Process missing blocks received from remote peer.
            :param message: A SendallMessage protobuf object.
        """
        start_time = time()

        # Emulate probability of crash in the middle of reconcilation.
        self.emulate_crash_probability()

        # Check if peer sent a request to add all their blocks
        nstate = self.request_handler.handle_add_block_request(message)
        end_time = time() - start_time
        recon_data = {'recon_time': end_time}

        print("----- %s VECTOR RECONCILIATION RESULTS -----\n\n" %

              self.userid)
        print("Total rec time %s secs" % recon_data['recon_time'])
        self.reconciliations.append(recon_data)
        return rstate.EVEN


    def print_reconciliation_stats(self):
        """
            Dummy holder for now.
        """
        print("Print all the stats!\n")
