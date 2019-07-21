from django.shortcuts import render, redirect
from django.http import HttpResponse
from django.contrib import messages
from django.contrib.auth.models import User, auth

from .models import Item, Person, Transaction, TwoPSet, shop


# Create your views here.

#def login(request):
 #   return redirect('/users/login')
'''
    if(request.method == 'POST'):
        userid = request.POST['userid']

        user = auth.authenticate(username = userid)

        if user is not None:
            auth.login(request, user)
            return redirect('/')
        else:    
            messages.info(request, 'invalid credentials')
            return redirect('login.html')

    else:
        return render(request, 'login.html')
'''

def index(request):
   
    # applist = Mock.applist
    applist = Transaction.objects.all()
    myShop = shop()
    items = myShop.display()
    return render(request, 'index.html', {'shoplist' : items})

def add(request):
    newTxn = Transaction()
    newTxn.payload = str(request.POST['newitem'])
    newTxn.TransactionID = shop.txns
    newTxn.isOn = True

    #push transaction to blockchain
    #shoplist = shoplist + newitem
    info = shop.info
    if newTxn.payload not in info:
        TwoP = TwoPSet()
        TwoP.addSet.add( (newTxn.TransactionID) )
        info.update({newTxn.payload: TwoP})
        shop.txns+=1
    #apply(newTxn) #because only 1 witness is necessary
    
    applist = Transaction.objects.all()

    
    return redirect('index')

def remove(request):
    info = shop.info
    item = str(request.POST[''])
    TwoP = info.get(remove)
    for i in TwoP.addSet:
        txnid = i
        break
    TwoP.removeSet.add(txnid)
    return redirect('index')


def apply(newTxn):

    txns = Transaction.objects.all()
    
    applist = Transaction.objects.all()

    num = 0
    for item in applist:
        if item.payload == newTxn.payload:
            item.isOn = 0
            item.save()
            num += 1

    newTxn.isOn = not (num % 2)
    newTxn.save()