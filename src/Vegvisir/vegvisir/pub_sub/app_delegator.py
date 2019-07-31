from vegvisir.blockchain.block import TransactionId
#from vegvisir.app.ShoppingList.shoplist.models import App
from vegvisir.pub_sub.abstract_app_delegator import VegvisirAppDelegator

class VirtualVegvisirAppDelegator(VegvisirAppDelegator):

    """
        :param instance: A VegvisirInstance for the application to use.
    """
    def __init__(self, instance, context):
         super().__init__(instance, context)

    def applyTransaction(topics : set(), payload : bytes, tx_id : TransactionId, deps : set()):

        item = payload[1:]
        operation = payload[0]
        #App.TwoP.updateSet(item, operation, App.TwoP)
        return

