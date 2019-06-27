import vegvisir.protos.vegvisirprotocol_pb2 as vgp
from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.emulator.socket_opcodes import (ProtocolStatus as ps,
                                              CommunicationStatus as comstatus)
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
    def __init__(self, request_creator, vector_clock, crash_prob):
        Protocol.__init__(self, request_creator.userid, request_creator.blockchain,
                          crash_prob)
        self.request_creator = request_creator
        self.network = request_creator.network
        self.reconciliations = [] 
        self.vector_clock = vector_clock

    def handle_peer_update(self, all_clocks, peer_conn):
        """
            Respond to a peer's update based on their clock.
            :param all_clocks: a vgp.VectorClock protobuf object.
            :param peer_conn: A Socket object.
        """
        # Emulate probability of crash in the middle of reconciliation.
        self.emulate_crash_probability()

        missing_hashes = self.vector_clock.compute_dependencies(all_clocks)
        print("MISSING HASHES %s\n" % missing_hashes)
        if missing_hashes != None:
            missing_blocks = []
            for block_hash in missing_hashes:
                missing_blocks.append(self.blockchain.blocks[block_hash])
            request = self.request_creator.add_blocks_request(
                                                       blocks=missing_blocks)

            status = self.network.send(request,
                                       destination=peer_conn,
                                       request_type="ADD BLOCK")
            if status == comstatus.SOCKET_ERROR:
                return ps.RECONCILIATION_FAILURE
            print("%s sent an ADD BLOCK request\n" % self.userid)

        # Emulate probability of crash in the middle of reconciliation.
        self.emulate_crash_probability()
  
        # Send an end protocol
        end_protocol = self.request_creator.end_protocol_request()
        status = self.network.send(end_protocol, destination=peer_conn,
                                   request_type="END PROTOCOL")
        if status == comstatus.SOCKET_ERROR:
            return ps.RECONCILIATION_FAILURE
        print("%s sent an END PROTOCOL request\n" % self.userid)

    def print_reconciliation_stats(self):
        """
            Dummy holder for now.
        """
        print("Print all the stats!\n")
