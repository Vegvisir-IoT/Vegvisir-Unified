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

class Mock:
    applist = ShoppingList()

    item1 = Item()
    item1.name = "apples"
    item1.isOn = True

    item2 = Item()
    item2.name = "bananas"
    item2.isOn = True

    user1 = Person()
    user1.name = "Me"
    user1.deviceID = "123"
    user1.transactionHeight = 1

    applist.items = [item1, item2]
    applist.people = [user1]

    txns = [Transaction()]
    t1 = Transaction()
    t1.id = 1 
    t1.payload = "oranges"
    t2 = Transaction()
    t2.id = 2
    t2.payload = "skrawranges"

    txns = [t1, t2]
    

