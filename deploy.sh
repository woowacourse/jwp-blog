REPOSITORY=/home/ubuntu/app
git pull
CURRENT_PID=$(lsof -t -i:8080 -s TCP:LISTEN)
if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> 구동중인 애플리케이션을 종료합니다."
    kill -9 $CURRENT_PID
    sleep 5
fi
echo "> 어플리케이션 빌드"
./gradlew clean build
cp ./build/libs/*.jar $REPOSITORY/
JAR_NAME=$(ls $REPOSITORY/ |grep 'blog' | tail -n 1)
java -jar $REPOSITORY/$JAR_NAME &