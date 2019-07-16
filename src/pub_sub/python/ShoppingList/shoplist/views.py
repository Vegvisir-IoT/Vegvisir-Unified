from django.shortcuts import render, redirect
from django.http import HttpResponse
from django.contrib import messages
from django.contrib.auth.models import User, auth

from .models import Item, Person, Transaction


# Create your views here.

def login(request):
    return redirect('/users/login')
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
    applist = Item.objects.all()
    return render(request, 'index.html', {'shoplist' : applist})

def add(request):
    newTxn = Transaction()
    newTxn.payload = str(request.POST['newitem'])

    #push transaction to blockchain
    #shoplist = shoplist + newitem
    
    apply(newTxn) #because only 1 witness is necessary

    applist = Item.objects.all()
    for x in applist:
        print(x.name)
    return redirect('index')


def apply(newTxn):

    txns = Transaction.objects.all()
    newItem = Item(name = newTxn.payload)
    #newItem.name = newTxn.payload
    
    applist = Item.objects.all()

    num = 0
    for item in applist:
        if item.name == newItem.name:
            item.isOn = 0
            item.save()
            num += 1

    newItem.isOn = not (num % 2)
    newItem.save()