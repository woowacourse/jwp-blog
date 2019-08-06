#!/bin/bash

# Build project
./gradlew clean build
java -jar ./build/libs/$(ls ./build/libs)