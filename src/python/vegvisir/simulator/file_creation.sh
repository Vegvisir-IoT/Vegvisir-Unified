#!/bin/bash

# Creates the file for each experiment
# The simulation output is redirected here
# Author: Gloire Rubambiza
# Version: 12/11/2018

user=$1
method=$2
search_dist=$3
day=$4

exp_name="Users_"$user"_"$method"_"$day"_"$search_dist".txt"
touch $exp_name
echo $exp_name