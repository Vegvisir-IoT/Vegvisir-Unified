from queue import Queue
from time import time, sleep


from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import TransactionId, Transaction
from vegvisir.pub_sub.abstract_vegvisir_instance import VegvisirInstance
from vegvisir.proto import vegvisir_pb2 as vegvisir


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief An interface class between the app and an emulated vegvisir node.
class VirtualVegvisirInstance(VegvisirInstance):

    """
       The representation of a vegvisir instance to be used by apps/servers.
       :param emulator: An Emulator object.
       :param limit: An int, the max number of transactions to put in a block.
    """
    def __init__(self, emulator, limit, topics=None):  
        self.emulator = emulator
        self.authorized_users = set(emulator.users)
        self.blockchain = emulator.request_creator.blockchain
        self.revoked_users = []
        self.transaction_limt = limit
        self.topics = topics
        self.outgoing_tx_queue = Queue()
        self.incoming_tx_queue = Queue()
        self.tx_list = []

        # Device specific things.
        self.device_to_tx_height = {}
        self.device_id = "Device B"
        self.height = 1
        self.subscription_list = {}


    def register_application_delegator(self, delegator):
        self.app_delegator = delegator


    def update_subscription_list(device_id, applicable_channels):
        """
           Update the subscription list for a given topic.
           :param device_id: A string.
           :param applicable_channels: A set of strings.
        """
        for topic in applicable_channels:
            if topic in self.subscription_list:
                if device_id in subscription_list[topic]:
                    continue
                else:
                    self.subscription_list[topic].add(device_id)
            else:
                self.subscription_list[topic] = set(device_id)
        print("Updated subscription list %s\n" % self.subscription_list)


    def add_transaction(self, context, topics, payload, dependencies):
        """
           The interface for the app to add a new transaction.
           :param context: A VegvisirAppContext object.
           :param topics: A set of strings.
           :param payload: A bytestring.
           :param dependencies: A set of TransactionId objects.
        """
        timestamp = time()   
        if self.height != 1:
            self.height += 1
        if not (self.device_id in self.device_to_tx_height):
            self.device_to_tx_height[self.device_id] = self.height
        self._add_transaction(self.device_id, self.height, topics, payload,
                              dependencies) 
        return True


    def _add_transaction(device_id, height, topics, payload, dependencies):
        """
           Append the transaction to the queue given the device_id and height.
           :param device_id: A string.
           :param height: An int.
           :param topics: A set of strings.
           :param payload: A bytes.
           :param dependencies: A Set of TransactionId objects.
        """
        deps = []
        for tx_id in dependencies:
            dep_pointer = vegvisir.Block.Transaction.TransactionId()
            dep_pointer.deviceId = tx_id.device_id
            dep_pointer.transactionHeight = tx_id.tx_height
            deps.append(dep_pointer)
        transaction = vegvisir.Block.Transaction(timestamp=time(),
                                                 dependencies=deps,
                                                 topics=topics,
                                                 payload=payload)
        transaction.transactionId.deviceId = self.device_id
        transaction.transactionId.transactionHeight = self.height
        self.device_to_tx_height.put(device_id, height + 1) 
        self.outoing_tx_queue.put(transaction)
        return True


    def poll_new_transactions(self):
        """
           Check if there are new transactions to be added to a block.
        """
        tx = None
        try:
            tx = self.outoing_tx_queue.get_nowait()
            self.tx_list.append(user_tx)
             
        except queue_is_empty:
            print("No txs currently enqueued...\n")
        
         # Check if we have enough transactions to make a block.
        if tx and len(self.block_queue) == self.transaction_limt:
            tx_list = []
            userid = self.tx_list[0].userid
            parents = self.blockchain.crdt.frontier_set()
            for tx in self.tx_list:
                userid = self.userid
                height = tx.transactionId.transactionHeight
                device_id = tx.transactionId.deviceId
                tx_id = TransactionId(height, device_id)
                timestamp = tx.timestamp
                comment = tx.payload
                tx_dict = {'recordid': height, 'comment': comment}
                if tx.HasField(depencencies): 
                    dependencies= []
                    for dependency in tx.dependencies:
                        _dependency = TransactionId(
                                                   dependency.transactionHeight,
                                                    dependency.deviceId)
                        dependencies.append(_dependency)
                        
                    user_tx = Transaction(userid, timestamp, tx_dict, tx_id,
                                          dependencies)
                else:
                    user_tx = Transaction(userid, timestamp, tx_dict)
                tx_list.append(user_tx)
            vegblock = Block(userid, time(), parents, self.tx_list)
            self.blockchain.add(block, Operation.ADDED_REQUEST)
            self.tx_list = []
        return True 

