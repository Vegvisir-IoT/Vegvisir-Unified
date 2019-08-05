from django.db import models
from django.contrib.postgres.fields import ArrayField
#from blockchain.block import block #transaction id and transaction

# Create your models here.

class Transaction(models.Model):
    TransactionID = models.IntegerField() #equals transaction height for now
    payload = models.CharField(max_length=100)
    isOn = models.BooleanField(default=False) #1 = add, 0 = remove
    dependencies = ArrayField(models.IntegerField(), default=list)
    #user = models.

class TransactionID():
    device_id : str
    tx_height : int