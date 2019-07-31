from vegvisir.blockchain.block import TransactionId
from vegvisir.pub_sub.abstract_app_delegator import VegvisirAppDelegator

class VirtualVegvisirAppDelegator(VegvisirAppDelegator):

    """
        :param instance: A VegvisirInstance for the application to use.
    """
    def __init__(self, instance, context):
        super().__init__(instance, context)

    def apply_transaction(self, topics, payload, tx_id, deps):
        for topic in topics:
            print("Interesting topic %s \n " % topic)
        print("Payload %s \n" % payload)

        item = payload[1:]
        operation = payload[0]

        App.TwoP.updateSet(item, operation, App.TwoP)
        return

