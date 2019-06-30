from django.db import models

# Create your models here.

class Item:
    name : str
    isOn : bool

class Transaction:
    TransactionID : int
    payload : str

class Person:
    name: str
    deviceID : str
    transactionHeight : int

class ShoppingList:
    items : [Item]
    people : [Person]



