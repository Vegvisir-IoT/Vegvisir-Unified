from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name = "index"),
    path('add', views.add, name='add'),
    path('apply', views.apply, name='apply'),
    #path('login', views.login, name='login')
]