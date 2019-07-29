from abc import ABCMeta, abstractmethod
from .vegvisir_instance import VirtualVegvisirInstance
from .application_context import VegvisirAppContext

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]

# @brief: An abstract class for vegvisir application delegators.
class VegvisirAppDelegator(metaclass=ABCMeta):

    """
        :param instance: A VegvisirInstance for the application to use.
    """
    def __init__(self, instance : VirtualVegvisirInstance, context : VegvisirAppContext):
         instance.register_application_delegator(context, self)
         self.vegvisir_instance = instance
         self.context = context

    @abstractmethod
    def applyTransaction(self, topics, payload, tx_id, deps):
        pass


