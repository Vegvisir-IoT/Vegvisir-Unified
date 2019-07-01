from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth.models import User, auth

from .models import Item, Person, ShoppingList, Transaction


# Create your views here.

def login(request):
    if(request.method == 'POST'):
        userid = request.POST['userid']

        user = auth.authenticate(username = userid)

        if user is not None:
            auth.login(request, user)
            return redirect('/')
        else:    
            messages.info(request, 'invalid credentials')
    else:
        return render(request, 'login.html')
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

def add(request):
    newTxn = Transaction()
    newTxn.payload = str(request.POST['newitem'])

    #push transaction to blockchain
    #shoplist = shoplist + newitem
    
    apply(request) #because only 1 witness is necessary
    return render(request, 'index.html')


def apply(request):

    txn = Transaction #valid transaction from blockchain
    newItem = Item()
    newItem.name = txn.payload

    return render(request, 'index.html', {'shoplist' : shoplist})