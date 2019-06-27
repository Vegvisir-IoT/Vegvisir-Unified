from google.protobuf.internal.encoder import _VarintBytes


import vegvisir.protos.vegvisirprotocol_pb2 as vgp
import vegvisir.proto.handshake as handshake
import vegvisir.proto.frontier as frontier
import vegvisir.proto.vegvisirNetwork as network
import vegvisir.proto.vegvisir as vegvisir
from vegvisir.blockchain.block import Block

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class that handles all protocol requests to peer.
class ProtocolRequestCreator(object):
    """
        A data structure for handling protocol requests.
    """
    def __init__(self, blockchain, network):
        self.blockchain = blockchain
        self.network = network
        self.userid = network.userid


    def create_update(self, local_vector_clock):
        """
           Create a vector clock update.
           :param vector_clock: A VectorClock object.
        """
        message = network.VegvisirProtocolMessage()
        update = vgp.Update()
        vectors = vgp.VectorClock()
        for userid, mapping in local_vector_clock.vector.items():
            vectors.vector_clocks.add(name=userid, leader=mapping['block'])
        vectors.send_limit = 5 
        update.current_view.CopyFrom(vectors)
        message.update.CopyFrom(update)
        return self.serialize_message(message) 


    def handshake_request(self, request_type, end_protocol=False,
                          protocol=None):
        """
            Create a handshake request.
            :param end_protocol: A boolean.
            :param request_type: A HandshakeMessage enum.
            :param protocol: A HandshakeMessage enum.
        """
        request = vegvisir.protocol.datatype.HandshakeMessage()
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
            :param protocol: The protocol spoken by the node.
            :param request_type: A HandshakeMessage.Type enum.
        """
        message = network.VegvisirProtocolMessage()
        request = handshake.HandshakeMessage()
        request.type = request_type 
        request.spokenVersions.extend([protocol])
        message.handshake.CopyFrom(request)
        return self.serialize_message(message)

    def frontier_set_request(self):
        """
            Creates a serialized frontier set request.
        """
        message = network.VegvisirProtocolMessage()
        request = frontier.Request()
        request.type = frontier.Request.SEND_FRONTIER_SET 
        local_frontier = self.blockchain.crdt.frontier_set()
        request.send.hashes.extend(list(local_frontier))
        message.frontier.request.CopyFrom(request)
        return self.serialize_message(message)


    def block_request(self, block_hash):
        """
            Creates a serialized block request.
            :param block_hash: A bytes object.
        """
        message = network.VegvisirProtocolMessage()
        request = frontier.Request()
        request.type = frontier.Request.SEND_BLOCK
        request.send = hashes.extend([block_hash])
        message.frontier.request.CopyFrom(request)
        return self.serialize_message(message)

    def add_blocks_request(self, blocks, end_protocol):
        """
            Creates a serialized request for the peer to add a pow block.
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
                outgoing_tx.comment = tx.comment
        
            # Add signature
            _block.signature.sha256WithEcdsa.byteString = block.sig
            blocks_to_add.append(_block)
        request.blockResponse.blocksToAdd.extend([blocks_to_add])
        message.frontier.request.CopyFrom(request)
        if end_protocol:
            message.endProtocol = True
        return self.serialize_message(message)

    def add_all_blocks_request(self):
        """
           Creates a serialized request for sending all blocks.
        """
        message = network.VegvisirProtocolMessage()
        request = self.request_stub(vgp.PeerRequest.ADD_BLOCK)
        ordered_blocks = self.blockchain.get_topological_sort()
        regular_blocks = []
        for block in ordered_blocks:
            if type(block) == Block:
                regular_blocks.append(block)
 
        for block in regular_blocks:
            pointer = request.blocks_to_add.add()
            pointer.vegblock.user_block.userid = str(block.userid)
            pointer.vegblock.user_block.timestamp.utc_time = block.timestamp
        
            # Implement location as needed here.

            # Add the parent hashes.
            for parent_hash in block.parents:
                ref_parent = pointer.vegblock.user_block.parents.add()
                ref_parent.hash.sha256 = parent_hash

            # Add the transactions
            for tx in block.tx:
                outgoing_tx = pointer.vegblock.user_block.transactions.add()
                outgoing_tx.userid = tx.userid
                outgoing_tx.timestamp = tx.timestamp
                outgoing_tx.recordid = tx.recordid
                outgoing_tx.comment = tx.comment
 
            # Add signature
            pointer.vegblock.signature.sha256WithEcdsa.byteString = block.sig
        message.request.CopyFrom(request)
        return self.serialize_message(message)
     

    def self_reconciliation_request(self, missing_hashes):
        """
            Creates a serialized reconciliation request.
            :param missing_hashes: A list of hashes to reconciled.
        """
        message = network.VegvisirProtocolMessage()
        request = self.request_stub(vgp.PeerRequest.SELF_RECONCILIATION_NEEDED)
        for block_hash in missing_hashes:
            missing_hash = request.target_hashes.add()
            missing_hash.hash = block_hash
        message.request.CopyFrom(request)
        return self.serialize_message(message)

    def peer_reconciliation_request(self, missing_hashes):
        """
            Creates a serialized reconciliation request.
            Lets the peer know we need to reconcile and triggers 
            the peer to enter their reconciliation method.
        """
        message = network.VegvisirProtocolMessage()
        request = self.request_stub(vgp.PeerRequest.PEER_RECONCILIATION_NEEDED)
        for block_hash in missing_hashes:
            requested_hash = request.target_hashes.add()
            requested_hash.hash = block_hash
        message.request.CopyFrom(request)
        return self.serialize_message(message)

    def end_protocol_request(self):
        """
            Creates a serialized request for ending the protocol.
        """
        message = network.VegvisirProtocolMessage()
        request = self.request_stub(vgp.PeerRequest.END_PROTOCOL)
        message.request.CopyFrom(request)
        return self.serialize_message(message)
       
        
