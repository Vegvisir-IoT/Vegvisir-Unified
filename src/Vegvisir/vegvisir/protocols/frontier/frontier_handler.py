from vegvisir.proto import (vegvisir_pb2 as vegvisir,
                           vegvisirCommon_pb2 as vc,
                           vegvisirNetwork_pb2 as network,
                           frontier_pb2 as frontier)

from vegvisir.emulator.socket_opcodes import ProtocolState as rstate
from vegvisir.blockchain.block import Transaction, Block

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

# @brief: A class that handles any Frontier messages.
class FrontierHandler(object):

    """
       :param private_key: An _EllipticCurvePrivateKey for signing blocks.
       :param frontier_server: A FrontierServer object.
       :param frontier_client: A FrontierClient object.
    """
    def __init__(self, private_key, frontier_server, frontier_client):
        self.private_key = private_key
        self.frontier_server = frontier_server
        self.frontier_client = frontier_client
        self.request_handler = self.frontier_client.request_handler
        self.request_creator = self.frontier_client.request_creator
        self.blockchain = self.request_creator.blockchain


    def handle_message(self, message, state):
        """
           :param message: A FrontierMessage protobuf object.
           :param state: A dictionary.
        """
        # Channel the message to the right handler.
        message_type = message.WhichOneof("frontier_message_type")
        if message_type == "request":
           request = frontier.Request()
           request.CopyFrom(message.request)
           request_type = request.type
           if request_type == frontier.Request.SEND_FRONTIER_SET: 
               return self.frontier_client.handle_fset_request(request.send,
                                                               state)
           elif request_type == frontier.Request.SEND_BLOCK:
               block_response = self.handle_block_request(request)
               state['message_queue'].put(block_response)
               return rstate.LOCAL_DOMINATES
           elif request_type == frontier.Request.ADD_BLOCK:
               self.request_handler.handle_add_block_request(request)
               return rstate.EVEN
           elif request_type == frontier.Request.RECONCILIATION_NEEDED:
               self.frontier_client.handle_reconciliation_request(
                                                           request.send.hashes,
                                                           state)
               return rstate.REMOTE_DOMINATES 
        else: # response
            response = frontier.Response()
            response.CopyFrom(message.response)
            response_type = response.WhichOneof("response_types")
            if response_type == "hashResponse":
               return self.frontier_server.handle_client_fset_update(
                                            list(response.hashResponse.hashes),
                                                                         state,
                                            server_is_subset=response.is_subset)
            if response_type == "blockResponse":
                block = self.retrieve_block(response)
                return self.handle_incoming_missing_block(block, state)


    def handle_block_request(self, request):
        """
            Handle a request for a block on the chain.
            :param request: a frontier.Request protobuf message.
        """
        print("BLOCK REQUEST START\n")
        # Create response
        blocks_of_interest = vc.AddBlocks()
        for b_hash in request.send.hashes:
            general_block = vegvisir.Block()
            block = self.blockchain.blocks[b_hash]
            general_block.user_block.userid = block.userid
            general_block.user_block.timestamp.utc_time = block.timestamp
            
            # Add the location once the format is agreed upon.

            # List of parent references for this block
            for parent_hash in block.parents:
                b_parent = general_block.user_block.parents.add()
                b_parent.hash.sha256 = parent_hash

            for transaction in block.tx:
                tx = general_block.user_block.transactions.add()
                tx.userid = transaction.userid
                tx.timestamp = transaction.timestamp
                tx.recordid = transaction.recordid
                tx.payload = transaction.comment

            # Add the signature
            general_block.signature.sha256WithEcdsa.byteString = block.sig
            print("BLOCK CONTENT TO BE SENT ->")
            block.print_block()
            print("block hash to be sent %s\n" % block.hash())

            # Serialize the block and turn it into a charlotte block.
            charlotte_block = blocks_of_interest.blocksToAdd.add()
            charlotte_block.block = general_block.SerializeToString()
 
        response = frontier.Response()
        response.blockResponse.CopyFrom(blocks_of_interest)
        message = network.VegvisirProtocolMessage()
        message.frontier.response.CopyFrom(response)

        # serialize response
        return self.request_handler.serialize(message)


    def handle_incoming_missing_block(self, block, state):
        """
           Cache a missing block received from remote peer.
           :param block: A Block object.
           :param state: A dictionary.
        """
        print("ADDING MISSING BLOCK\n\n")
        block.print_block()
        missing_blocks = state['missing_blocks']
        state['cache'][block.hash()] = block 
        for parent_hash in block.parents:
            if not(parent_hash in self.blockchain.blocks):
                missing_blocks.append(parent_hash)
        if 'reconciled_blocks' in state.keys():
            state['reconciled_blocks'] += 1
        else:
            state['reconciled_blocks'] = 1
        return rstate.RECONCILIATION


    def provide_pow(self, state, end_protocol=False):
        """
           Add cached blocks when there are no more missing blocks.
           :param end_protocol: A boolean.
           :param state: A dictionary.
        """
        cached_blocks = state['cached_blocks']
        cached_blocks.reverse()
        self.blockchain.add_list(cached_blocks, state['cache'])
        print("%s Server adding from cache worked\n" % self.userid)

        frontier_set = self.blockchain.crdt.frontier_set()
        print("UPDATED FRONTIER SET: %s\n" % frontier_set)
    
        # The frontier_set in this case should be the most recent.
        pow_parents = frontier_set.union(state['other_fset'])
        pow_block = self.create_pow(pow_parents)
    
        # Add the POW block to our blockchain
        self.blockchain.add(pow_block, Operation.PROVIDED_POW)
        print("POW block ->")
        pow_block.print_block()
            
        # Create a request for the peer to add our POW block
        pow_request = self.request_creator.add_blocks_request([pow_block],
                                                              end_protocol)
        message_queue.put(pow_request)
        return rstate.RECONCILIATION


    def create_pow(self, pow_hashes):
        """
            Create a proof-of-witness block based on the block received
            from a peer.
           :param pow_hashes: A set of block hashes.
        """
        print("SERVER %s CREATING POW BLOCK\n" % self.userid)
        parents = list(pow_hashes)
        print("PARENTS OF POW ARE %s\n" % parents)
        tx_dict = {'recordid': randint(1, 60000), 'comment': b'Type: POW;'}
        new_tx = Transaction(self.userid, time(), tx_dict)
        block = Block(self.userid, time(), parents, [new_tx])
        block.sign(self.private_key)
        return block


    def request_next_missing_block(self, state):
     
        """
           Send a request for the next block and update the state.
           :param state: A dictionary.
        """
        missing_blocks = state['missing_blocks']
        if len(missing_blocks) == 0:
            return rstate.EVEN

        bhash = missing_blocks.pop(0)
        if bhash in state['cache']:
             block = state['cache'][bhash]
             for parent_hash in block.parents:
                 missing_blocks.append(parent_hash)
        else:
            # Create a request the block.
            state['cached_blocks'].append(bhash)
            block_request = self.request_creator.block_request(bhash)
            # Add the request to the message queue for the connection.
            state['message_queue'].put(block_request) 
        return rstate.REMOTE_DOMINATES



    def retrieve_block(self, response):
        """
             Retrieve the Vegvisir block inside of a charlotte block.
             :param response: A FrontierMessage protobuf object.
         """
         # Check what is set inside the response
        charlotte_blocks = response.blockResponse
        for block_bytes in charlotte_blocks.blocksToAdd:
            vegvisir_block = vegvisir.Block()
            vegvisir_block.ParseFromString(block_bytes.block)
            user_block = vegvisir.Block.UserBlock()
            user_block.CopyFrom(vegvisir_block.user_block)

            # Extract the userid and timestamp
            userid = user_block.userid
            timestamp = user_block.timestamp.utc_time

            # Worry about location later

            # Add parent list.
            parent_list = []
            for parent_hash in user_block.parents:
                parent_list.append(parent_hash.hash.sha256)

            # Add transaction list.
            tx_list = []
            for transaction in user_block.transactions:
                tx_userid = transaction.userid
                tx_timestamp = transaction.timestamp
                tx_recordid = transaction.recordid
                tx_comment = transaction.payload
                tx_dict = {'recordid': tx_recordid, 'comment': tx_comment}
                incoming_tx = Transaction(tx_userid, tx_timestamp, tx_dict)
                tx_list.append(incoming_tx)
    
            # Add the signature
            sig = vegvisir_block.signature.sha256WithEcdsa.byteString
 
            # Reconstruct the block
            incoming_block = Block(userid, timestamp, parent_list, tx_list)
            incoming_block.sig = sig
            # incoming_block.print_block()
            return incoming_block
