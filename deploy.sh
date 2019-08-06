#!/bin/bash

# Build project
git pull
./gradlew clean build
java -jar ./build/libs/$(ls ./build/libs)
