#!/bin/bash

kill $(lsof -t -i:8080)
git pull
./gradlew clean build
java -jar ./build/libs/$(ls ./build/libs) &
