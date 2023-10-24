#! /usr/bin/env bash

DIST_DIR=build

javac -d $DIST_DIR -cp myscheduler/src/main/java:myscheduler/src/test/java:testutils/src/main/java $1

echo
echo "Successful build."
echo
echo
