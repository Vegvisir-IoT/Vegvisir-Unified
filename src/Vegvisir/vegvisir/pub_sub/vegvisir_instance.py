from queue import Queue
from time import time, sleep
from queue import Empty as queue_is_empty

from vegvisir.simulator.opcodes import Operation
from vegvisir.blockchain.block import TransactionId, Transaction, Block
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
    def __init__(self, emulator, limit):  
        self.emulator = emulator
        self.authorized_users = set(emulator.users)
        self.blockchain = emulator.request_creator.blockchain
        self.revoked_users = []
        self.transaction_limit = limit
        self.outgoing_tx_queue = Queue()
        self.incoming_tx_queue = Queue()
        self.tx_list = []

        # Device specific things.
        self.device_to_tx_height = {}
        self.device_id = "Device B"
        self.height = 0
        self.subscription_list = {}
        self.last_tx = None


    def register_application_delegator(self, context, delegator):
        """
           Register a delegator and context to handle new txs for the app.
           :param context: A VegvisirAppContext object.
           :param delegator: VirtualVegvisirAppDelegator object
        """
        self.app_context = context 
        self.app_delegator = delegator


    def update_subscription_list(self, device_id, applicable_channels):
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


    def add_transaction(self, context, topics, payload, dependencies, userid : str):
        """
           The interface for the app to add a new transaction.
           :param context: A VegvisirAppContext object.
           :param topics: A set of strings.
           :param payload: A bytestring.
           :param dependencies: A set of TransactionId objects.
           :param userid: a str representing the user
        """
        timestamp = time()   
        self.height += 1
        if not (self.device_id in self.device_to_tx_height):
            self.device_to_tx_height[self.device_id] = self.height
        self._add_transaction(self.device_id, self.height, topics, payload,
                              dependencies, userid) 
        return True


    def _add_transaction(self, device_id, height, topics, payload, dependencies, userid : str):
        """
           Append the transaction to the queue given the device_id and height.
           :param device_id: A string.
           :param height: An int.
           :param topics: A set of strings.
           :param payload: A bytes.
           :param dependencies: A Set of TransactionId objects.
           :param userid: a str representing the user
        """
        deps = []
        for tx_id in dependencies:
            if isinstance(tx_id, TransactionId):
                dep_pointer = vegvisir.Block.Transaction.TransactionId()
                dep_pointer.deviceId = tx_id.device_id
                dep_pointer.transactionHeight = tx_id.tx_height
                deps.append(dep_pointer)
        transaction = vegvisir.Block.Transaction(userid=userid, timestamp=time(),
                                                 dependencies=deps,
                                                 topics=topics,
                                                 payload=payload)
        #print("Topics to add %s\n" % transaction.topics)
        #print("Dependencies to add %s\n" % transaction.dependencies)
        #transaction.dependencies.extend(dependencies)
        transaction.transactionId.deviceId = self.device_id
        transaction.transactionId.transactionHeight = self.height
        #print("Adding tx %s\n" % transaction.__str__())
        self.last_tx = TransactionId(self.height, self.device_id)
        self.device_to_tx_height[device_id] = height + 1 
        self.outgoing_tx_queue.put(transaction)
        print("Putting transaction in the queue, height %s, size %s\n\n" % (self.height, self.outgoing_tx_queue.qsize())) 
        return True


    def poll_new_transactions(self):
        """
           Check if there are new transactions to be added to a block.
        """
        while True:
            # sleep(5)
            tx = None
            try:
                tx = self.outgoing_tx_queue.get_nowait()
                self.tx_list.append(tx) 
                #print("THIS FARRRRRRRRRRRRRRRRRRRRRRR \n")
            except queue_is_empty:
                print("No txs to poll\n")
            
            # Check if we have enough transactions to make a block.
            if tx != None and len(self.tx_list) == self.transaction_limit:
                userid = self.tx_list[0].userid
                parents = self.blockchain.crdt.frontier_set()
                tx_list = []
                for tx in self.tx_list:
                    userid = tx.userid
                    height = tx.transactionId.transactionHeight
                    device_id = tx.transactionId.deviceId
                    tx_id = TransactionId(height, device_id)
                    timestamp = tx.timestamp
                    comment = tx.payload
                    topics = tx.topics
                    tx_dict = {'recordid': height, 'comment': comment}
                    if len(tx.dependencies) > 0: 
                        dependencies= []
                        for dependency in tx.dependencies:
                            _dependency = TransactionId(
                                                    dependency.transactionHeight,
                                                        dependency.deviceId)
                            dependencies.append(_dependency)
                        user_tx = Transaction(userid, timestamp, tx_dict, tx_id=tx_id, topics=topics,
                                            dependencies=dependencies)
                        user_tx.print_tx()
                    else:
                        user_tx = Transaction(userid, timestamp, tx_dict, tx_id=tx_id, topics=topics,
                                              dependencies=[])
                        user_tx.print_tx()
                    tx_list.append(user_tx)
                vegblock = Block(userid, time(), parents, tx_list)
                vegblock.sign(self.emulator.private_key)
                vegblock.print_block()
                self.blockchain.add(vegblock, Operation.ADDED_REQUEST)
                self.tx_list = []
        return True 

