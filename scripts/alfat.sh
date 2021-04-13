#!/bin/bash
#The path to the ALFAT jar file excluding ALFAT.jar
FILEPATH="/Path/To/Jar/Not/Including/Jar/"
#Absolute path of the file that was passed
FILE="$PWD/$1"
#Some errors may occur without changing the directory
cd $FILEPATH
if [ $# -eq 1 ]
  then
    #If there is one command line argument open with a file
    java -jar ALFAT.jar $FILE
else
  #If there is not one argument open alfat with default options
  java -jar ALFAT.jar
fi
#Set the directory back to where it was
cd $PWD