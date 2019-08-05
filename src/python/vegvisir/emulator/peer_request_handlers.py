from time import time
from random import randint


from google.protobuf.internal.encoder import _VarintBytes


import vegvisir.protos.vegvisirprotocol_pb2 as vgp
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
        :param network: A SimulationNetworkOperator object.
        :param client: A Socket object.
        :param vector_clock: A VectorClock object.
        :param protocol_spoken: A string.
    """

    def __init__(self, blockchain, network, vector_clock, protocol_spoken):
        self.blockchain = blockchain
        self.network = network
        self.userid = network.userid
        self.vector_clock = vector_clock
        if protocol_spoken == "VECTOR":
           self.protocol = vgp.VECTOR 
        elif protocol_spoken == "FRONTIER":
           self.protocol = vgp.FRONTIER
        else:
           self.protocol = vgp.SEND_ALL 


    def __pick_highest_version(self, response, choice):
        """
           Fill the protocol list response with the most efficient version
           spoken by a node.
           :param response: A vgp.PeerResponse object.
           :param choice: A vgp.ProtocolVersion object.
        """
        versions_spoken = vgp.ProtocolVersions()
        versions_spoken.protocols.extend([choice])
        response.protocol_version_response.CopyFrom(versions_spoken)


    def serialize_and_send(self, response, peer_conn=None, server=True):
        """
            Serialize a response and pass it to server for sending.
            :param response: bytes.
            :param peer_conn: a Socket through which to talk with the peer.
            :param server: a boolean.
            :rtype: boolean.
        """
        if server:
            print("Serializing a response for the server\n")
        else:
            print("Serializing a response for the client\n")
        size = response.ByteSize()
        response_bytes = _VarintBytes(size)
        response_bytes += response.SerializeToString()
        if server:
            return self.network.send(response_bytes, destination=peer_conn)
        else:
            return self.network.send(response_bytes)


    def handle_fset_request(self, peer_conn=None, server=True):
        """
            Handle a request for the blockchain frontier set.
            :param peer_conn: a Socket through which to talk with the peer.
        """
        # Create response
        my_frontier_set = vgp.FrontierSet()
        for b_hash in self.blockchain.crdt.frontier_set():
            frontier_block = my_frontier_set.block_hashes.add()
            frontier_block.hash = b_hash
        response = vgp.PeerResponse()
        response.frontier_set_response.CopyFrom(my_frontier_set)
        message = vgp.VegvisirMessage()
        my_frontier_set = vgp.FrontierSet()
        message.response.CopyFrom(response)

        # Serialize and send response
        status = self.serialize_and_send(message, peer_conn, server)
        return self.check_status(status, "Frontier set")
        

    def handle_bc_hashes_request(self, peer_conn, server=True):
        """
            Handle a request for blockchain hashes.
            :param peer_conn: a Socket through which to talk with the peer.
            :param server: a boolean.
        """
        print("BC HASHES REQUEST START\n")
        # Create response
        my_blockdag_hashes = vgp.SendBlockchainHashes()
        for b_hash in self.blockchain.blocks.keys():
            block_hashes = my_blockdag_hashes.hash_set.add()
            block_hashes.hash = b_hash
        response = vgp.PeerResponse()
        response.bc_hashes_response.CopyFrom(my_blockdag_hashes)
        message = vgp.VegvisirMessage()
        message.response.CopyFrom(response)

        # Serialize and send response
        status = self.serialize_and_send(message, peer_conn, server)
        return self.check_status(status, "BC hashes")

    def handle_block_request(self, request, peer_conn, server=True):
        """
            Handle a request for a block on the chain.
            :param request: a PeerRequest object.
            :param peer_conn: a Socket through which to talk with the peer.
            :param server: a boolean.
        """
        print("BLOCK REQUEST START\n")
        # Create response
        blocks_of_interest = vgp.SendBlock()
        for b_hash in request.target_hashes:
            block = self.blockchain.blocks[b_hash.hash]
            vegvisir = blocks_of_interest.blocks.add()
            vegvisir.vegblock.user_block.userid = block.userid
            vegvisir.vegblock.user_block.timestamp.utc_time = block.timestamp
            
            # Add the location once the format is agreed upon.

            # List of parent references for this block
            for parent_hash in block.parents:
                b_parent = vegvisir.vegblock.user_block.parents.add()
                b_parent.hash.sha256 = parent_hash

            for transaction in block.tx:
                tx = vegvisir.vegblock.user_block.transactions.add()
                tx.userid = transaction.userid
                tx.timestamp = transaction.timestamp
                tx.recordid = transaction.recordid
                tx.comment = transaction.comment

            # Add the signature
            vegvisir.vegblock.signature.sha256WithEcdsa.byteString = block.sig
            print("BLOCK CONTENT TO BE SENT ->")
            block.print_block()
            print("block hash to be sent %s\n" % block.hash())
            # add the signature once the format is agreed upon.
        response = vgp.PeerResponse()
        response.send_block_response.CopyFrom(blocks_of_interest)
        message = vgp.VegvisirMessage()
        message.response.CopyFrom(response)

        # serialize and send response
        status = self.serialize_and_send(message, peer_conn, server)
        return self.check_status(status, "block")


    def handle_protocol_list_request(self, request, peer_conn):
        """
           handle a request for the list of protocols spoken.
           :param request: a PeerRequest object. 
           :param peer_conn: a socket object.
        """
        response = vgp.PeerResponse()
        choice = ""
        sendall = vgp.SEND_ALL
        vector = vgp.VECTOR
        frontier = vgp.FRONTIER
        latest = vgp.LATEST_HAPPENINGS
        no_choice = vgp.VERSION

        if vector in request.protocol_list and self.protocol == vector:
            choice = vector 
            self.__pick_highest_version(response, choice)
        elif latest in request.protocol_list and self.protocol == latest:
            choice = latest 
            self.__pick_highest_version(response, choice)
        elif frontier in request.protocol_list and self.protocol == frontier:
            choice = frontier
            self.__pick_highest_version(response, choice)
        elif sendall in request.protocol_list and self.protocol == sendall:
            choice = sendall 
            self.__pick_highest_version(response, choice)
        else: # We don't agree on a protocol 
            choice = no_choice 
            self.__pick_highest_version(response, choice)

        print("Server, List received from client %s\n" % 
              request.protocol_list)
        print("SERVER PROTOCOL CHOICE %s\n" % choice) 

        message = vgp.VegvisirMessage()
        message.response.CopyFrom(response)

        # Serialize and send response
        status = self.serialize_and_send(message, peer_conn)
        return self.check_status(status, "Protocol"), choice

    def handle_add_block_request(self, request):
        """
            Handle a request for adding a peer's blocks to the chain.
            :param request: a PeerRequest object.
        """
        # Rebuild and add the block to the blockchain 
        for incoming in request.blocks_to_add:
            b_userid = incoming.vegblock.user_block.userid
            b_timestamp = incoming.vegblock.user_block.timestamp.utc_time
            
            # Location to be added as needed
            
            # Create a list of parents
            b_parents = []
            for parent_reference in incoming.vegblock.user_block.parents:
                b_parents.append(parent_reference.hash.sha256)
            
            # Create a list of transactions
            tx_list = []
            for transaction in incoming.vegblock.user_block.transactions:
                userid = transaction.userid
                timestamp = transaction.timestamp
                tx_dict = {'recordid': transaction.recordid,
                           'comment': transaction.comment}
                incoming_transaction = Transaction(userid, timestamp, tx_dict)
                tx_list.append(incoming_transaction)              
            
            # Create the new block to be added
            newblock = Block(b_userid, b_timestamp, b_parents, tx_list)
            newblock.sig = incoming.vegblock.signature.sha256WithEcdsa.byteString
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
                if self.protocol == vgp.VECTOR and block_not_on_chain:
                    update_local_vector_maps(self.vector_clock, newblock)

        self.vector_clock.update_offline_activity(False)
        return ps.SUCCESS

        # Update the number of users that have "witnessed a block"
        # To be implemented as needed.
        
    def check_status(self, status, request_type):
        """
            :param status: A tuple.
            :param request_type: A string.
        """
        if status == comstatus.SOCKET_ERROR: 
            print("Handling of %s request failed!\n" %
                   request_type)
            return ps.RECONCILIATION_FAILURE 
        else:
            return ps.SUCCESS
