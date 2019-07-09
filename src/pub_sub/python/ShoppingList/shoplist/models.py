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

class ShoppingList(models.Model):
    items = [Item]
    people = [Person]

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

    

