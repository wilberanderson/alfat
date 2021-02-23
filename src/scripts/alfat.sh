#!/bin/bash
FILEPATH="/home/chloe/IdeaProjects/alfat/out/artifacts/ALFAT_jar/"
FILE="$PWD/$1"
cd $FILEPATH
if [ $# -eq 1 ]
  then
    java -jar ALFAT.jar $FILE
else
  java -jar ALFAT.jar
fi
