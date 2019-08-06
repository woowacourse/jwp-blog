BRANCH="dpudpu"
echo ">>> GIT PULL ..."
git pull origin $BRANCH
echo ">>> BUILD START ..."
gradlew clean build
echo ">>> BUILD COMPLETE"
CURRENT_PID=$(pgrep -f java)
echo ">>> KILL PROCESS $CURRENT_PID"
kill -2 $CURRENT_PID
JAR_NAME=$(ls /home/ubuntu/jwp-blog/build/libs/ | grep 'myblog' | tail -n 1)
echo ">>> START APPLICATION..."
nohup java -jar build/libs/$JAR_NAME &