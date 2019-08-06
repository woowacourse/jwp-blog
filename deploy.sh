#!/bin/bash

kill $(lsof -t -i:8080)
cd jwp-blog
git pull
./gradlew clean build
java -jar ./build/libs/$(ls ./build/libs) &
