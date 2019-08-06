#!/bin/sh

buildDirectory=build/libs

echo ">git pull 실행"

git pull origin

echo ">build"

./gradlew clean build

cd ${buildDirectory}

echo ">stop exist execution"

fuser -k 8080/tcp

recentFile=$(ls -1t | head -n 1)

echo ${recentFile};

nohup java -jar ${recentFile} &