git pull origin heebong
fuser -k 8080/tcp
./gradlew clean build
cd build/libs
java -jar myblog-0.0.1-SNAPSHOT.jar &