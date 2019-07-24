from time import time
from random import randint


from google.protobuf.internal.encoder import _VarintBytes


from vegvisir.proto import (handshake_pb2 as hs, vegvisir_pb2 as vegvisir,
                            frontier_pb2 as frontier,
                            vegvisirNetwork_pb2 as network) 
from vegvisir.simulator.opcodes import Operation as op
from vegvisir.blockchain.block import Block, Transaction
from vegvisir.blockchain.blockchain_helpers import int_to_bytestring
from vegvisir.emulator.emulation_helpers import update_local_vector_maps
from vegvisir.emulator.socket_opcodes import (ProtocolStatus as ps,
                                              CommunicationStatus as comstatus)


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles peer requests.
class PeerRequestHandler(object):
    """
        A data structure representing a handle on blockchain operations.
        :param blockchain: A Blockchain object {blockhash -> Block}.
        :param vector_clock: A VectorClock object.
        :param protocol_spoken: A string.
    """

    def __init__(self, blockchain, vector_clock, protocol_spoken):
        self.blockchain = blockchain
        self.vector_clock = vector_clock
        self.userid = self.vector_clock.userid
        if protocol_spoken == "VECTOR":
           self.protocol = hs.VECTOR 
        elif protocol_spoken == "FRONTIER":
           self.protocol = hs.FRONTIER
        else:
           self.protocol = hs.SEND_ALL 



    def serialize(self, response):
        """
            Serialize a response.
            :param response: one of handshake, sendall, vector,
                            or frontier message.
            :rtype: bytestring.
        """
        size = response.ByteSize()
        response_bytes = _VarintBytes(size)
        response_bytes += response.SerializeToString()
        return response_bytes


    def handle_fset_request(self, remote_is_subset):
        """
            Handle a request for the blockchain frontier set.
            :param remote_is_subset: A boolean.
        """

        # Create response
        frontier_response = frontier.Response()
        f_set = list(self.blockchain.crdt.frontier_set())
        frontier_response.hashResponse.hashes.extend(f_set)
        frontier_response.is_subset = remote_is_subset
        
        message = network.VegvisirProtocolMessage()
        my_frontier_set = frontier.FrontierMessage()
        my_frontier_set.response.CopyFrom(frontier_response)
        message.frontier.CopyFrom(my_frontier_set) 

        # Serialize response
        return self.serialize(message)


    def handle_protocol_list_request(self, message):
        """
           handle a request for the list of protocols spoken.
           :param message: a HandshakeMessage object. 
        """
        choice = ""
        sendall = hs.SEND_ALL
        vector = hs.VECTOR
        frontier = hs.FRONTIER
        no_choice = hs.VERSION

        if vector in message.spokenVersions and self.protocol == vector:
            choice = vector 
        elif frontier in message.spokenVersions and self.protocol == frontier:
            choice = frontier
        elif sendall in message.spokenVersions and self.protocol == sendall:
            choice = sendall 
        else: # We don't agree on a protocol 
            choice = no_choice 

        print("Server, List received from client %s\n" % 
              message.spokenVersions)
        print("SERVER PROTOCOL CHOICE %s\n" % choice) 

        return choice, message.type


    def handle_add_block_request(self, request):
        """
            Handle a request for adding a peer's blocks to the chain.
            :param request: a vegvisir.protocol.datatype.Request message.
        """
        # Rebuild and add the block to the blockchain 
        for incoming in request.add.blocksToAdd:
            general_block = vegvisir.Block()
            general_block.ParseFromString(incoming.block)
            user_block = general_block.user_block
            b_userid = user_block.userid
            b_timestamp = user_block.timestamp.utc_time
            
            # Location to be added as needed
            
            # Create a list of parents
            b_parents = []
            for parent_reference in user_block.parents:
                b_parents.append(parent_reference.hash.sha256)
            
            # Create a list of transactions
            tx_list = []
            for transaction in user_block.transactions:
                userid = transaction.userid
                timestamp = transaction.timestamp
                tx_dict = {'recordid': transaction.recordid,
                           'comment': transaction.payload}
                incoming_transaction = Transaction(userid, timestamp, tx_dict)
                tx_list.append(incoming_transaction)              
            
            # Create the new block to be added
            newblock = Block(b_userid, b_timestamp, b_parents, tx_list)
            newblock.sig = general_block.signature.sha256WithEcdsa.byteString
            is_pow = any([b'Type: POW' in tx.comment for tx in tx_list])
            if is_pow:
                print("POW Block to be added: ")
                newblock.print_block()
                self.blockchain.add(newblock, op.GOT_POW)
            else:
                print("User block to be added: ")
                newblock.print_block()

                # Check if the block is on the chain.
                # This ensures we don't update vector clocks unless the 
                # block was not on the chain to begin with.
                block_on_chain = newblock.hash() in self.blockchain.blocks.keys()
                block_not_on_chain = not(block_on_chain)

                if block_not_on_chain:
                    self.blockchain.add(newblock, op.RECEIVED_REGULAR_BLOCK) 
                if self.protocol == hs.VECTOR and block_not_on_chain:
                    update_local_vector_maps(self.vector_clock, newblock)

        self.vector_clock.update_offline_activity(False)
        return ps.SUCCESS

        # Update the number of users that have "witnessed a block"
        # To be implemented as needed.
        
