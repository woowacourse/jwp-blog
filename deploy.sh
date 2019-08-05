#!/bin/bash

REPOSITORY=/home/ubuntu/git

cd $REPOSITORY/jwp-blog/

echo "> Git Pull"

git pull 

echo "> 프로젝트 Build 시작"

./gradlew clean build

echo "> Build 파일 복사"

cp ./build/libs/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f java)

echo "$CURRENT_PID"

for p in `sudo lsof -n -i:8080 | grep LISTEN | awk '{print $2}'`; do sudo kill -9 $p; done
sleep 5

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls $REPOSITORY/ |grep 'myblog' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar $REPOSITORY/$JAR_NAME &

/home/ubuntu/./slack.sh "배포 완료"