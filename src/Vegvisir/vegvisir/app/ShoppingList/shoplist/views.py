from django.shortcuts import render, redirect
from django.http import HttpResponse
from django.contrib import messages
from django.contrib.auth.models import User, auth

from .models import Transaction, TwoPSet, shop, TransactionTuple


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
    myShop = shop()
    items = myShop.display()
    for i in items:
        print(i.txnid)
    return render(request, 'index.html', {'shoplist' : items})

def add(request):
    newTxn = Transaction()
    newTxn.payload = str(request.POST['newitem'])
    newTxn.TransactionID = shop.txns
    #set timestamp -- get system time

    #push transaction to blockchain
    #shoplist = shoplist + newitem
    info = shop.info
    if newTxn.payload not in info:
        TwoP = TwoPSet()
        TwoP.addSet.add( (newTxn.TransactionID) )
        #newTxn.deps += [shop.lastOp]
        info.update({newTxn.payload: TwoP})
        shop.lastOp = newTxn.TransactionID
        shop.txns+=1
    else: 
        TwoP = info.get(newTxn.payload)
        #newTxn.deps += [shop.lastOp]
        if len(TwoP.removeSet) != 0:
            info.get(newTxn.payload).addSet.add((newTxn.TransactionID))
            shop.txns+=1
    #apply(newTxn) #because only 1 witness is necessary

    
    return redirect('index')

def remove(request):
    info = shop.info
    item = request.POST['payloadtoremove']
    txnid = int(request.POST['txnidtoremove'])
    TwoP = info.get(item)
    #print(info)
    TwoP.removeSet.add(txnid)
    return redirect('index')


def apply(newTxn):
    newTxn.isOn = not (num % 2)
    newTxn.save()