from vegvisir.design_pattern.observer import Observer


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief A simple class to observe the blockchain for any new updates.
class WatchDog(Observer):

    def __init__(self, incoming_tx_queue, topics):
        """
           :param incoming_tx_queue: a Queue.
           :param topics: A set of strings.
        """
        self.incoming_tx_queue = incoming_tx_queue
        self.topics = topics


    def update(self, observable, arg):
         """
            Update applications about transactions
            of interest to them coming from new blocks.
            :param observable: A Blockchain object.
            :param arg: A Block object.
         """
         # Check if the application is subscribed to any topics.
         block_topics = [tx.topics for tx in arg.tx]
         if not self.topics:
             return
         elif any(topic in self.topics for topic in block_topics):
             for tx in arg.tx:
                 for topic in tx.topics:
                     if topic in self.topics:
                         if not tx in self.incoming_tx_queue:
                             self.incoming_tx_queue.put(tx)

