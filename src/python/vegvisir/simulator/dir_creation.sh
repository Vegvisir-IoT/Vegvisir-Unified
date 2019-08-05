#!/bin/bash

# Creates some directory for user, method, etc
# Author: Gloire Rubambiza
# Version: 12/11/2018

name=$1
if ! test -d $name
    then
        mkdir $name
elif test -d $name
    then
        rm -r $name
        mkdir $name
fi
echo $name