from abc import ABCMeta, abstractmethod


__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: An abstract class for apps to interface with Vegvisir.
class VegvisirInstance(metaclass=ABCMeta):

    @abstractmethod
    def register_application_delegator(context, delegator):
        """
           Register a delegator to handle new transactions for the app.
           :param context: A VegvisirAppContext object .
           :param delegator: VirtualVegvisirAppDelegator object
        """
        pass


    @abstractmethod
    def add_transaction(context, topics, payload, dependencies, userid):
        """
            Add a new valid transaction to the DAG.
            :param context: A VegvisirAppContext object.
            :param topics: A set of strings.
            :param payload: A bytestring.
            :param dependencies: A set of TransactionId objects.
            :param userid: a str representing the user
        """
        pass
