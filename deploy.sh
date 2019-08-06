#!/bin/bash

REPOSITORY=/home/ubuntu/git

cd $REPOSITORY/jwp-blog/

echo "> Git Pull"

git pull

echo "> 프로젝트 Build 시작"

./gradlew clean build

echo "> Build 파일 복사"

cp ./build/libs/*.jar $REPOSITORY/jwp-blog/

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f myblog-0.0.1)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -2 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls $REPOSITORY/jwp-blog/ | grep 'myblog' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar $REPOSITORY/jwp-blog/$JAR_NAME &