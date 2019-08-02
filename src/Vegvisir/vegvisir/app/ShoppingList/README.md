# Running the app

Make sure you are in this directory ('app/ShoppingList'). This directory has the manage.py script you need in order torun the server. 

The command to use is: *python manage.py runserver*

Once you have run this command, if you have no errors, you should see something like this:

*System check identified no issues (0 silenced).
August 02, 2019 - 18:12:11
Django version 2.2.2, using settings 'ShoppingList.settings'
Starting development server at http://127.0.0.1:8000/
Quit the server with CONTROL-C.*

Then you can view this app in your browser at '*localhost:8000*' or '*127.0.0.1:8000*'


If you type "*python manage.py*" it will give you a list of other commands you can run besides 'runserver'



# General

## ShoppingList is the main app that everything runs from. 
It contains settings.py:
-setting for the entire app
-contains info about installed apps, middleware, root urls, templates, databases, etc

It also contains the main urls.py for the app. If you make a subapp and want to be able to access it you have to add it in the urlpatterns of this urls.py first. For example, if I want to access an app '*users*', I have to make sure to put '*path('users/', include('users.urls'))*' in urlpatterns. '*users/*' is path that you have to add onto '*localhost:8000' to be able to access views of the users app. '*users.urls*' is the urls.py of the '*users*' app.

## There are 2 sub apps: shoplist and users.
**'shoplist' app** 
Has the views.py that gets user info and displays, models.py that initializes the vegvisir instance, delegator, emulator, watch

**'users' app**
deals with registering, logining in, and logging out
 
## HTML files and static files
'*app/ShoppingList/templates*' contains the html templates

'*app/ShoppingList/assets*' contains css and other static files

Link to django tutorial: [Link](https://www.youtube.com/watch?v=SIyxjRJ8VNY&list=PLsyeobzWxl7r2ukVgTqIQcl-1T0C2mzau) 
