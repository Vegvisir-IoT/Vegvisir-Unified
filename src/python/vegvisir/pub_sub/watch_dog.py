from vegvisir.design_pattern.observer import Observer


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief A simple class to observe the blockchain for any new updates.
class WatchDog(Observer):

    def __init__(self, instance, topics):
        """
           :param incoming_tx_queue: a Queue.
           :param topics: A set of strings.
        """
        self.instance = instance
        self.topics = topics


    def update(self, observable, arg):
         """
            Update applications about transactions
            of interest to them coming from new blocks.
            :param observable: A Blockchain object.
            :param arg: A Block object.
         """
         # Check if the application is subscribed to any topics.
         block_topics = []
         for tx in arg.tx:
            block_topics += tx.topics
         print("block topics: %s \n" % block_topics)
         if len(self.topics) == 0:
             return
         elif any(topic in self.topics for topic in block_topics):
             for tx in arg.tx:
                 for topic in tx.topics:
                     if topic in self.topics:
                        topics = set([topic])
                        payload = tx.comment
                        tx_id = tx.tx_id
                        deps = tx.dependencies
                        self.instance.app_delegator.apply_transaction(topics, payload, tx_id, deps)


