from django.shortcuts import render
from django.http import HttpResponse

from .models import Item, Person, ShoppingList

# Create your views here.
def index(request):

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

    shoplist = ShoppingList()
    shoplist.items = [item1, item2]
    shoplist.people = [user1]
    
    return render(request, 'index.html', {'shoplist' : shoplist})

def apply(request):
    return