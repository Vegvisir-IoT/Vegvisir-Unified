from vegvisir.blockchain.block import TransactionId
from vegvisir.pub_sub.abstract_app_delegator import VegvisirAppDelegator

class VirtualVegvisirAppDelegator(VegvisirAppDelegator):

    """
        :param instance: A VegvisirInstance for the application to use.
    """
    def __init__(self, instance, context):
        super().__init__(instance, context)
        self.twoP = TwoPSet()
        self.items = []

    def apply_transaction(self, topics, payload, tx_id, deps):
        '''
            Breaks payload up into str item and int operation (payload[0]).

            Adds this str item to the self.items which is a list of applied transactions
            that the app will display.
            
            Uses payload to update self.twoP

            :param topics: set of str topics app is interested in
            :param payload: bytestring of app specific info
            :param tx_id: TransactionId of this transaction
            :param deps: set of TransactionIds of transactions this transaction depends on
        '''
        item = payload[1:].decode("utf-8")
        print("ITEM TO ADD ---- %s" % item)
        print(payload[0])
        
        self.twoP.updateSets(item, payload[0])
        difference = self.twoP.addSet - self.twoP.removeSet
        if item in difference and item not in self.items:
            self.items += [item]

        #gets rid of duplicates from list
        for item in self.items:
            if item in self.twoP.removeSet or item not in self.twoP.addSet:
                self.items.remove(item)
        return

class TwoPSet():
    addSet : set()
    removeSet : set()

    def __init__(self):
        self.addSet = set()
        self.removeSet = set()

    def lookup(self, x) -> bool:
        return (x in addSet) and not(x in removeSet)
    
    def updateSets(self, payload, op):
        '''
            Updates add and remove sets of this 2P set with this payload based on the operation
            :param payload: str
            :param op: int
        '''
        txn = set({payload})
        if op:
            self.addSet = self.addSet.union(txn)
            self.removeSet = self.removeSet.difference(txn)
        else:
            self.addSet = self.addSet.difference(txn)
            self.removeSet = self.removeSet.union(txn)
            
