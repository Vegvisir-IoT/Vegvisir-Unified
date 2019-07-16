from google.protobuf.internal.encoder import _VarintBytes


from vegvisir.blockchain.crypto import sign, verify_signature
from vegvisir.blockchain.block import Block
from vegvisir.blockchain.blockchain_helpers import (int_to_bytestring,
                                                     str_to_bytestring)
from vegvisir.proto import (handshake_pb2 as hs, frontier_pb2 as frontier,
                            sendall_pb2 as sa, vector_pb2 as vp,
                            vegvisirNetwork_pb2 as network,
                            charlotte_pb2 as charlotte,
                            vegvisir_pb2 as vegvisir)

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles all protocol requests to peer.
class ProtocolRequestCreator(object):
    """
        A data structure for handling protocol requests.
    """
    def __init__(self, blockchain, network, private_key):
        self.blockchain = blockchain
        self.network = network
        self.userid = network.userid
        self.private_key = private_key


    def create_update(self, local_vector_clock):
        """
           Create a vector clock update.
           :param local_vector_clock: A VectorClock object.
        """
        message = network.VegvisirProtocolMessage()
        vectors = vp.VectorClock()
        bytestring = bytearray()
        for userid, mapping in local_vector_clock.vector.items():
            vectors.clocks[userid] = mapping['block']
            bytestring += str_to_bytestring(userid)
            bytestring += int_to_bytestring(mapping['block'])
        vectors.sendLimit = 5 
        bytestring += int_to_bytestring(5)
        signature = sign(bytestring, self.private_key)
        vectors.signature = signature
        pub_key = self.blockchain.keystore.get_public_key(self.userid)
        vectors.publicKey = pub_key
        message.vector.worldView.CopyFrom(vectors)
        return self.serialize_message(message) 


    def handshake_request(self, request_type, end_protocol=False,
                          protocol=None):
        """
            Create a handshake request.
            :param end_protocol: A boolean.
            :param request_type: A HandshakeMessage enum.
            :param protocol: A HandshakeMessage enum.
        """
        request = hs.HandshakeMessage()
        if not end_protocol:
            request.type = request_type
            request.spokenVersions.extend([protocol])
        message = network.VegvisirProtocolMessage()
        if end_protocol:
            message.endProtocol = True
        message.handshake.CopyFrom(request) 
        return self.serialize_message(message)


    def serialize_message(self, request):
        """
            Compute the size of the request for serialization purposes.
            :param request: one of handshake, sendall, vector,
                            or frontier message.
        """
        size = request.ByteSize()
        bytestring = _VarintBytes(size)
        bytestring += request.SerializeToString()
        return bytestring

    def initiate_protocol_request(self, protocol, request_type):
        """
            Create a serialized protocol initialization request.
            :param protocol: A HandshakeMessage.ProtocolVersion enum.
            :param request_type: A HandshakeMessage.Type enum.
        """
        message = network.VegvisirProtocolMessage()
        request = hs.HandshakeMessage()
        request.type = request_type 
        request.spokenVersions.extend([protocol])
        message.handshake.CopyFrom(request)
        return self.serialize_message(message)

    def frontier_set_request(self):
        """
            Create a serialized frontier set request.
        """
        message = network.VegvisirProtocolMessage()
        request = frontier.Request()
        request.type = frontier.Request.SEND_FRONTIER_SET 
        local_frontier = self.blockchain.crdt.frontier_set()
        request.send.hashes.extend(list(local_frontier))
        message.frontier.request.CopyFrom(request)
        print("Frontier request to be sent %s\n" % message.__str__())
        return self.serialize_message(message)


    def block_request(self, block_hash):
        """
            Create a serialized block request.
            :param block_hash: A bytes object.
        """
        message = network.VegvisirProtocolMessage()
        request = frontier.Request()
        request.type = frontier.Request.SEND_BLOCK
        request.send.hashes.extend([block_hash])
        message.frontier.request.CopyFrom(request)
        return self.serialize_message(message)


    def add_blocks_request(self, blocks, end_protocol):
        """
            Create a serialized request for the peer to add a pow block.
            :param blocks: A list of Block objects.
            :param end_protocol: A boolean.
        """
        message = network.VegvisirProtocolMessage()
        request = frontier.Request()
        blocks_to_add = []
        for block in blocks:
            _block = vegvisir.Block.UserBlock()
            _block.userid = block.userid
            _block.timestamp.utc_time = block.timestamp
        
            # Implement location as needed here.
        
            # Add the parent hashes.
            for parent_hash in block.parents:
                ref_parent = _block.parents.add()
                ref_parent.hash.sha256 = parent_hash

            # Add the transactions
            for tx in block.tx:
                outgoing_tx = _block.transactions.add()
                outgoing_tx.userid = tx.userid
                outgoing_tx.timestamp = tx.timestamp
                outgoing_tx.recordid = tx.recordid
                outgoing_tx.payload = tx.comment
        
            # Add signature
            _block.signature.sha256WithEcdsa.byteString = block.sig
            blocks_to_add.append(_block)
        request.blockResponse.blocksToAdd.extend(blocks_to_add)
        message.frontier.request.CopyFrom(request)
        if end_protocol:
            message.endProtocol = True
        return self.serialize_message(message)


    def add_all_blocks_request(self):
        """
           Create a serialized request for sending all blocks.
        """
        message = network.VegvisirProtocolMessage()
        sa_message = sa.SendallMessage() 
        ordered_blocks = self.blockchain.get_topological_sort()
        regular_blocks = ordered_blocks[1:]
        blocks_to_add = []
 
        for block in regular_blocks:
            general_block = vegvisir.Block()
            _block = vegvisir.Block.UserBlock()
            _block.userid = str(block.userid)
            _block.timestamp.utc_time = block.timestamp
        
            # Implement location as needed here.

            # Add the parent hashes.
            for parent_hash in block.parents:
                ref_parent = _block.parents.add()
                ref_parent.hash.sha256 = parent_hash

            # Add the transactions
            for tx in block.tx:
                outgoing_tx = _block.transactions.add()
                outgoing_tx.userid = tx.userid
                outgoing_tx.timestamp = tx.timestamp
                outgoing_tx.recordid = tx.recordid
                outgoing_tx.payload = tx.comment
 
            # Add signature
            general_block.signature.sha256WithEcdsa.byteString = block.sig
            general_block.user_block.CopyFrom(_block)
            serialized_block = general_block.SerializeToString()
            charlotte_block = charlotte.Block()
            charlotte_block.block = serialized_block
            blocks_to_add.append(charlotte_block)
        sa_message.add.blocksToAdd.extend(blocks_to_add)
        message.sendall.CopyFrom(sa_message)
        return self.serialize_message(message)
     

    def reconciliation_request(self, missing_hashes):
        """
            Create a serialized reconciliation request.
            :param missing_hashes: A list of hashes to be reconciled.
        """
        message = network.VegvisirProtocolMessage()
        request = frontier.Request()
        request.type = frontier.Request.RECONCILIATION_NEEDED
        request.send.hashes.extend(missing_hashes)
        message.frontier.request.CopyFrom(request)
        return self.serialize_message(message)


    def end_protocol_request(self):
        """
            Create a serialized request for ending the protocol.
        """
        message = network.VegvisirProtocolMessage()
        request = self.request_stub(vgp.PeerRequest.END_PROTOCOL)
        message.request.CopyFrom(request)
        return self.serialize_message(message)
       
