#!/bin/bash

kill $(lsof -t -i:8080)
git pull
~/jwp-blog/gradlew clean build
java -jar ~/jwp-blog/build/libs/$(ls ~/jwp-blog/build/libs) &
