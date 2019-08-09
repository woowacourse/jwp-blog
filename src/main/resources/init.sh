#!/bin/bash

sudo apt-get update
sudo apt-get install default-jdk
sudo apt-get install mysql-server
sudo apt-get install git
cd /home/ubuntu/
git clone -b level4 https://github.com/codeanddonuts/jwp-blog.git
cd jwp-blog
./gradlew clean build
cd build/libs
sudo service mysql start
java -jar *.jar