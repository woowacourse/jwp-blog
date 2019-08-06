sudo apt-get update

sudo apt-get -y install openjdk-8-jdk

JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

mkdir app

mkdir app/git

cd ~/app/git

git clone -b yk1028 https://github.com/yk1028/jwp-blog.git

cd jwp-blog

REPOSITORY=/home/ubuntu/app/git

cd $REPOSITORY/jwp-blog/

echo "> Git Pull"

git pull

echo "> 프로젝트 Build 시작"

./gradlew build

echo "> jar 실행"
java -jar build/libs/*.jar