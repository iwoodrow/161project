#!/usr/bin/env bash

if [ $# -ne 1 ]; then
  echo "Usage: ./runsample.sh JAVASOURCEFILE"
  exit
fi

# echo "Compiling and running the Java file $1..."
java ${1%%.*} < sample3.in > sample3.out
