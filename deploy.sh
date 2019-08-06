REPOSITORY=/home/ubuntu/app
git pull
CURRENT_PID=$(pgrep -f MyblogApplication)
if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
       kill -9 $CURRENT_PID
       sleep 5
fi
./gradlew clean build
cp ./build/libs/*.jar $REPOSITORY/
JAR_NAME=$(ls $REPOSITORY/ |grep 'MyblogApplication' | tail -n 1)
java -jar $REPOSITORY/myblog-0.0.1-SNAPSHOT.jar &