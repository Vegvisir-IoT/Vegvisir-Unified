from random import randint, uniform
from abc import ABCMeta, abstractmethod

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

#@brief: An abstract class that all protocols can use
class Protocol(metaclass=ABCMeta):

    """
       :param userid: A string.
       :param blockchain: A Blockchain object.
    """
    def __init__(self, userid, blockchain, crash_prob):
        self.userid = userid
        self.blockchain = blockchain
        self.crash_prob = crash_prob
        # Might want to add this later.
      # self.reconciliations = []

    @abstractmethod
    def print_reconciliation_stats(self):
        pass


    def emulate_crash_probability(self):
        """
           Generate a boolean emulating whether the node crashes.
        """
        
        crash_prob = uniform(0,1)
        if crash_prob <= self.crash_prob:
            print("Node %s crashed with %s probability!\n" % (self.userid,
                           self.crash_prob))
            print("Generated probability %s\n" % crash_prob)
            print("Exiting now..")
            exit(1)
