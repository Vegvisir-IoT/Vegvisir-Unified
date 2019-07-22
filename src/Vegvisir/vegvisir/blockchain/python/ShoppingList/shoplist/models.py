from django.db import models
from django.contrib.postgres.fields import ArrayField
#from blockchain.block import block #transaction id and transaction

# Create your models here.

class Item(models.Model):
    name = models.CharField(max_length=100)
    isOn = models.BooleanField(default=False)

class Transaction(models.Model):
    TransactionID = models.IntegerField() #equals transaction height for now
    payload = models.CharField(max_length=100)
    isOn = models.BooleanField(default=False) #1 = add, 0 = remove
    dependencies = ArrayField(models.IntegerField(), default=list)
    #user = models.

class TransactionID():
    device_id : str
    tx_height : int

class Person(models.Model):
    name= models.CharField(max_length=100)
    deviceID = models.CharField(max_length=300)
    transactionHeight = models.IntegerField()

class TransactionTuple():
    payload : str
    txnid : int
    
    def __init__(self, payload, txnid):
        self.payload = payload
        self.txnid = txnid

class shop():
    info = dict()
    txns = 0

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
            
class TwoPSet():
    addSet : set()
    removeSet : set()

    def __init__(self):
        self.addSet = set()
        self.removeSet = set()

    def lookup(x) -> bool:
        return (x in addSet) and not(x in removeSet)
    
    def update(isOn, txnid):
        if isOn:
            addSet.update(txnid)
            removeSet.discard(txnid)
        else:
            removeSet.update(txnid)
            addSet.discard(txnid)
    

    

    

