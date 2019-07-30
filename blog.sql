create database blog_test default character set utf8 collate utf8_general_ci;

show databases;
use blog_test; -- 이 명령이 없으면 밖에서 안보이나??

create user 'springuser'@'%' identified by 'aaaa'; -- Creates the user
grant all on blog_test.* to 'springuser'@'%'; -- Gives all the privileges to the new user on the newly created database

create user 'aaaa' identified by 'aaaa'; -- Creates the user
grant all on blog_test.* to 'aaaa'; -- Gives all the privileges to the new user on the newly created database

show tables;
select * from user;