from django.shortcuts import render, redirect
from django.http import HttpResponse
from django.contrib import messages
from django.contrib.auth.models import User, auth

from .models import App

from vegvisir.blockchain.block import Transaction, TransactionId
#from pubsub.VegInstance import VegInstance
from vegvisir.blockchain.blockchain_helpers import (int_to_bytestring, double_to_bytestring,
                                 str_to_bytestring)

# Create your views here.
def index(request):
    '''
        Renders view with all valid transactions attempted by the user.
        
        :param request: an html request
    '''
    show = App.txn_history
    
    return render(request, 'index.html', {'shoplist' : show})

def apply(request):
    '''
        Renders view of transactions that have been pushed down to the blockchain, 
        been witnessed, and applied.

        :param request: an html request
    '''
    #show = App.twoP.addSet.difference(App.twoP.removeSet)
    #show = App.vegInstance.app_delegator.twoP.addSet - App.vegInstance.app_delegator.twoP.removeSet
    show = App.vegInstance.app_delegator.items
    
    return render(request, 'apply.html', {'shoplist' : show})

def add(request):
    '''
        Sends a input from user to pub-sub where it will be turned into a transaction 
        and send down to the blockchain.

        Also adds user input into the list of transactions they have made so far.

        :param request: an html request
    '''
    #get item name and operation from POST request
    item = str(request.POST['item'])
    
    if 'remove' in request.POST:
        operation = 0
        App.txn_history.append(str(item)+': '+'was removed')
    else:
        alr_added_deleted = App.txn_history.count(item + ': was added') ==  App.txn_history.count(item + ': was removed')
        if item + ': was added' not in App.txn_history or alr_added_deleted:
            operation = 1
            App.txn_history.append(str(item)+': '+'was added')
        else: #item is added before it is removed = nop
            return redirect('index')
  
    payload = bytes().join( [bytes([operation]), bytes(item, 'utf-8')] ) #turns operation and item name into bytestring
    
    #userid = request.user.username

    deps = [App.last_tx_id]

    App.vegInstance.add_transaction(App.context, App.topics, payload, deps, 'Alpha')
    App.last_tx_id = App.vegInstance.last_tx #gets txid of tx that was just added

    return redirect('index')
 