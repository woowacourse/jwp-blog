#!/bin/bash

cd /home/ubuntu/
sudo apt-get update
sudo apt-get install default-jdk
sudo apt-get install mysql-server
sudo mysql -uroot << QUERIES
CREATE DATABASE woowa;
CREATE USER 'donut'@'localhost' identified by 'qwer1234';
GRANT ALL PRIVILEGES ON woowa.* to 'donut'@'localhost';
QUERIES
sudo apt-get install git
git clone -b level4 https://github.com/codeanddonuts/jwp-blog.git
cd jwp-blog
./gradlew clean build
cd build/libs
sudo service mysql start
java -jar *.jar