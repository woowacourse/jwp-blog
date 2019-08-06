myblog_pid=`pgrep -f myblog*`

echo $myblog_pid
kill -2 $myblog_pid

git pull origin Deocksoo
sudo chmod +x gradlew
sudo ./gradlew clean build
java -jar build/libs/myblog-0.0.1-SNAPSHOT.jar
