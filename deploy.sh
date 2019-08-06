#!/bin/bash

REPOSITORY=/home/ubuntu

echo "> move [jwp-blog] directory"
cd $REPOSITORY/jwp-blog/

echo "> [GIT] PULL"
git pull

echo "> [GRADLE] Project BUILD START"
./gradlew build

echo "> [SYSTEM] BUILD file copy"
cp ./build/libs/*.jar $REPOSITORY/

echo "> [SYSTEM] application pid check"
CURRENT_PID=$(pgrep -f jwp-blog)
echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> [SYSTEM] 현재 구동중인 어플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> [SYSTEM] kill -2 $CURRENT_PID"
    kill -2 $CURRENT_PID
    sleep 5
fi

echo "> [SYSTEM] new application deploy"
JAR_NAME=$(ls $REPOSITORY/ |grep 'jwp-blog' | tail -n 1)

echo "> [SYSTEM] JAR Name: $JAR_NAME"

nohup java -jar $REPOSITORY/$JAR_NAME &