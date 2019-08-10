#!/bin/bash
MYSQL_ROOT_PASSWORD=qwer1234
MYSQL=$(grep 'temporary password' /var/log/mysqld.log | awk '{print $11}')

cd ~
sudo apt-get update -y
sudo apt-get install -y expect
sudo apt-get install -y aptitude
sudo apt-get install -y mysql-server
sudo apt-get install -y default-jdk

SECURE_MYSQL=$(expect -c "
set timeout 10
  spawn mysql_secure_installation

  expect \"Enter password for user root:\"
  send \"$MYSQL\r\"
  expect \"New password:\"
  send \"$MYSQL_ROOT_PASSWORD\r\"
  expect \"Re-enter new password:\"
  send \"$MYSQL_ROOT_PASSWORD\r\"
  expect \"Change the password for root ?\ ((Press y\|Y for Yes, any other key for No) :\"
  send \"y\r\"
  send \"$MYSQL\r\"
  expect \"New password:\"
  send \"$MYSQL_ROOT_PASSWORD\r\"
  expect \"Re-enter new password:\"
  send \"$MYSQL_ROOT_PASSWORD\r\"
  expect \"Do you wish to continue with the password provided?\(Press y\|Y for Yes, any other key for No) :\"
  send \"y\r\"
  expect \"Remove anonymous users?\(Press y\|Y for Yes, any other key for No) :\"
  send \"y\r\"
  expect \"Disallow root login remotely?\(Press y\|Y for Yes, any other key for No) :\"
  send \"n\r\"
  expect \"Remove test database and access to it?\(Press y\|Y for Yes, any other key for No) :\"
  send \"y\r\"
  expect \"Reload privilege tables now?\(Press y\|Y for Yes, any other key for No) :\"
  send \"y\r\"
  expect eof
  ")

  echo $SECURE_MYSQL

sudo mysql -uroot <<MYSQL_SCRIPT
CREATE DATABASE woowa;
CREATE USER 'donut'@'localhost' IDENTIFIED BY 'qwer1234';
GRANT ALL PRIVILEGES ON woowa.* TO 'donut'@'localhost';
FLUSH PRIVILEGES;
MYSQL_SCRIPT

mkdir repositories
cd repositories
git clone https://github.com/codeanddonuts/jwp-blog.git

cd ~/repositories/jwp-blog
pwd
git pull origin level4
./gradlew clean build
cd build
cd libs
java -jar myblog-0.0.1-SNAPSHOT.jar &