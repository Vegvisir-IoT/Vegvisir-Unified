from django.db import models
from django.contrib.postgres.fields import ArrayField

from vegvisir.blockchain.block import TransactionId
#from pubsub.Context import Context
from vegvisir.pub_sub.application_context import VegvisirAppContext
from vegvisir.pub_sub.vegvisir_instance import VirtualVegvisirInstance
from vegvisir.pub_sub.app_delegator import VirtualVegvisirAppDelegator
from vegvisir.emulator.emulate_vegvisir import Emulator
from vegvisir.pub_sub.watch_dog import WatchDog

from concurrent.futures import ThreadPoolExecutor
# Create your models here.

class Txn():
    payload : str
    operation : bool #1 = add, 0 = remove
    txnid : TransactionId
    pay2 : list

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
      
class App():

    fromAdd = False

    context = VegvisirAppContext('shopping list', 'a shopping list', set(['costco']))
    topics = set(['costco']) #allow user to select/add new topics
    lastTxnID = None
    twoP = TwoPSet() #contains dependency add and remove sets

    args = {'username': 'Alpha', 'chainfile' : 'gloirechain.txt', 'crash_prob' : 0.0, 'block_limit' : 1, 'protocol' : 'sendall', 'paramsfile' : 'gloireparams.txt'}
    emulator = Emulator(args)
    emulator.activate_gossip_layer()
    emulator.blockchain.synchronize_functions()
    vegInstance = VirtualVegvisirInstance(emulator, 1)
    delegator = VirtualVegvisirAppDelegator(vegInstance, context)

    vegInstance.register_application_delegator(context, delegator)
    watch_dog = WatchDog(vegInstance, context.channels)
    emulator.blockchain.add_observer(watch_dog)
    pool = ThreadPoolExecutor(2)
    pool.submit(vegInstance.poll_new_transactions)

'''
class Transaction():
    TransactionID : int
    deps : list
    payload : str
    timestamp : str
    userid : str


class TransactionID():
    deviceid : str
    tx_height : int


class TransactionTuple():
    payload : str
    txnid : int
    
    def __init__(self, payload, txnid):
        self.payload = payload
        self.txnid = txnid

class shop():
    info = dict()
    txns = 1
    lastOp = 0

    def display(self) -> list():
        shown = []
        for k,v in self.info.items():
            diff = v.addSet - v.removeSet
            if not( len(diff) == 0):
                txnid = self.getValfromSet(diff)
                t = TransactionTuple(k, txnid) #tuple of payload value and txn id
                print(t)
                shown += [t]
        return shown

    def getValfromSet(self, x):
        for i in x:
            result = i
            return result
            

'''
    

    

