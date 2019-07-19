from django.db import models
from django.contrib.postgres.fields import ArrayField

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

class TransactionID:
    device_id : str
    tx_height : int

class Person(models.Model):
    name= models.CharField(max_length=100)
    deviceID = models.CharField(max_length=300)
    transactionHeight = models.IntegerField()

class shop():
    info = dict()
    txns = 0

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

    

    

