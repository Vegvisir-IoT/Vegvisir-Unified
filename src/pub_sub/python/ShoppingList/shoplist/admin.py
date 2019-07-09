from django.contrib import admin
from .models import Item, Person, Transaction, ShoppingList
# Register your models here.
admin.site.register(Item)
admin.site.register(Person)
admin.site.register(Transaction)
admin.site.register(ShoppingList)