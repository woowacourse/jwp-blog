REPOSITORY="/home/ec2-user"
cd $REPOSITORY/jwp-blog

BRANCH="step4"
echo ">>> GIT PULL ..."
git pull origin $BRANCH

echo ">>> BUILD START ..."
./gradlew clean build
echo ">>> BUILD COMPLETE"

echo ">>> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f myblog)
echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
	echo ">>> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
	echo ">>> KILL PROCESS $CURRENT_PID"
	kill -9 $CURRENT_PID
fi

echo ">>> START NEW APPLICATION..."
JAR_NAME=$(ls $REPOSITORY/jwp-blog/build/libs/ | grep 'myblog' | tail -n 1)
echo ">>> JAR NAME: $JAR_NAME"
nohup java -jar build/libs/$JAR_NAME &
