from django.shortcuts import render, redirect
from django.http import HttpResponse
from django.contrib import messages
from django.contrib.auth.models import User, auth

from .models import TwoPSet, app #Transaction, TwoPSet, shop, TransactionTuple

from vegvisir.blockchain.block import Transaction, TransactionId
#from pubsub.VegInstance import VegInstance
from vegvisir.pub_sub.vegvisir_instance import VirtualVegvisirInstance
from vegvisir.pub_sub.app_delegator import VirtualVegvisirAppDelegator
from vegvisir.emulator.emulate_vegvisir import Emulator

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
    show = app.TwoP.addSet.difference(app.TwoP.removeSet)
    print(request.user.username)

    return render(request, 'index.html', {'shoplist' : show})


def add(request):

    #filling in transaction fields
    item = str(request.POST['item'])
    print(item)
    if 'remove' in request.POST:
        operation = 0
    else:
        operation = 1
    deps = [app.lastTxnID]

    TwoP = TwoPSet()
    TwoP.updateSet(item, operation, app.TwoP)
    
    payload = bytes().join( [bytes([operation]), bytes(item, 'utf-8')] )
    #push to vegvisir at this pt
    userid = request.çuser.username
    args = {'username': userid, 'chainfile' : 'gloirechain.txt', 'crash_prob' : 0.0, 'block_limit' : 1, 'protocol' : 'sendall'}
    emulator = Emulator(args)
    emulator = emulator.activate_gossip_layer()
    emulator.blockchain.synchronize_functions()
    vegI = VirtualVegvisirInstance(emulator, 1)
    delegator = VirtualVegvisirAppDelegator(vegI, app.context)
    vegI.add_transaction(app.context, app.topics, payload, deps, userid)

    lastTxnID = 'item'
    return redirect('index')
    
def remove(request):
    
    return redirect('index')

'''
def add(request):

    #filling in transaction fields
    newTxn = Txn()
    newTxn.payload = str(request.POST['newitem'])
    newTxn.operation = 1
    app.txnHeight = app.txnHeight+1    
    newTxn.txnid = TransactionId(app.txnHeight, app.deviceID)
    
    newTxn.pay2 = [app.lastTxnID]
    
    
    app.lastTxnID = newTxn.txnid

    app.things += [newTxn]
    #push to vegvisir at this pt
    return redirect('index')
    
def remove(request):
    #filling in transaction fields
    newTxn = Txn()
    newTxn.payload = str(request.POST['payloadtoremove'])
    newTxn.operation = 0
    app.txnHeight = app.txnHeight+1
    newTxn.txnid = TransactionId(app.txnHeight, app.deviceID)
    
    newTxn.pay2 = [app.lastTxnID]
    
    app.lastTxnID = newTxn.txnid

    app.things += [newTxn]
    #push to vegvisir at this pt
    return redirect('index')

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
'''