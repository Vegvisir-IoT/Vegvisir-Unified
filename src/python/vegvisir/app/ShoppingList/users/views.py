from django.shortcuts import render, redirect
from django.contrib import messages
from django.contrib.auth.models import User, auth

# Create your views here.
def register(request):
    
    if request.method == 'POST':
        userid = request.POST['userid']
        password = request.POST['pw']
        password2 = request.POST['pw2']

        if password == password2:
            if User.objects.filter(username = userid).exists():
                messages.info(request, 'UserID Taken')
                return redirect('register')
            else:
                user = User.objects.create_user(username = userid, password = password)
                user.save()
                print("user created")

                return redirect('login')
        else:
            messages.info(request, 'Passwords do not match')
            return redirect('register')
    else:
        return render(request, 'register.html')


def login(request):
    if request.method == 'POST':
        userid = request.POST['userid']
        pw = request.POST['pw']
        user = auth.authenticate(username = userid, password = pw)

        if user is not None:
            auth.login(request, user)
            return redirect('/')
        else:
            messages.info(request, 'User not found')
            return redirect('login')
    else:
        return render(request, 'login.html')

def logout(request):
    auth.logout(request)
    return redirect('/')