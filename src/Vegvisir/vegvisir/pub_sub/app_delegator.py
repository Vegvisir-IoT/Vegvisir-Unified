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
        for topic in topics:
            print("Interesting topic %s \n " % topic)
        
        item = payload[1:].decode("utf-8")
        print("ITEM TO ADD ---- %s" % item)
        print(payload[0])
        # operation = int.from_bytes(payload[0], byteorder="big")
        # print("OPERATIONSSS ---- type %s -----" % operation)
        self.twoP.updateSet(item, payload[0], self.twoP)
        
        if item not in self.twoP.removeSet and item in self.twoP.addSet:
            self.items += [item]

        for item in self.items:
            if item in self.twoP.removeSet or item not in self.twoP.addSet:
                self.items.remove(item)
        #put every item in list and if item not in set difference remove from list, so display not random
        #App.TwoP.updateSet(item, operation, App.TwoP)
        return

class TwoPSet():
    addSet : set()
    removeSet : set()

    def __init__(self):
        self.addSet = set()
        self.removeSet = set()

    def lookup(self, x) -> bool:
        return (x in addSet) and not(x in removeSet)
    
    def updateSet(self,payload, op, twoPset):
        txn = set({payload})
        if op:
            twoPset.addSet = twoPset.addSet.union(txn)
            twoPset.removeSet = twoPset.removeSet.difference(txn)
        else:
            twoPset.addSet = twoPset.addSet.difference(txn)
            twoPset.removeSet = twoPset.removeSet.union(txn)
            
