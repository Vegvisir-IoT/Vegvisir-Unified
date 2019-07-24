from abc import ABCMeta, abstractmethod


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

# @brief: An abstract class for vegvisir application delegators.
class VegvisirAppDelegator(metaclass=ABCMeta):

     """
        :param instance: A VegvisirInstance for the application to use.
     """
     def __init__(self, instance):
         self.vegvisir_instance = instance


    @abstractmethod
    def applyTransaction(self, topics, payload, tx_id, deps):
        pass


