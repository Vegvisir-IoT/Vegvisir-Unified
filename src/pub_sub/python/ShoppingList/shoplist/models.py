from django.db import models

# Create your models here.

class Item(models.Model):
    name = models.CharField(max_length=100)
    isOn = models.BooleanField(default=False)

class Transaction(models.Model):
    TransactionID = models.IntegerField()
    payload = models.TextField()

class Person(models.Model):
    name= models.CharField(max_length=100)
    deviceID = models.CharField(max_length=300)
    transactionHeight = models.IntegerField()


    

