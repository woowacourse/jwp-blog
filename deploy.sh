branch_name=$1
my_blog=`pgrep -f mybolg*`

echo $1
echo $my_blog
kill -2 $my_blog

git pull origin $1;
sudo chmod +x gradlew;
sudo ./gradlew clean build;
java -jar build/libs/myblog-0.0.1-SNAPSHOT.jar;
