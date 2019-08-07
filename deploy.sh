# git pull
git pull origin wbluke

# build
./gradlew clean build

# kill last server
for p in `sudo lsof -n -i:8080 | grep LISTEN | awk '{print $2}'`; do sudo kill -9 $p; done

# deploy new server
sudo java -jar ./build/libs/myblog-0.0.1-SNAPSHOT.jar
