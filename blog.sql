create database blog_test default character set utf8 collate utf8_general_ci;

show databases;
use blog_test; -- 이 명령이 없으면 밖에서 안보이나??

create user 'springuser'@'%' identified by 'aaaa'; -- Creates the user
grant all on blog_test.* to 'springuser'@'%'; -- Gives all the privileges to the new user on the newly created database

create user 'aaaa' identified by 'aaaa'; -- Creates the user
grant all on blog_test.* to 'aaaa'; -- Gives all the privileges to the new user on the newly created database

show tables;
select * from user;
delete from user WHERE id = 2;
drop table user;
drop table sns_info; --  



select * from article;
select * from comment;


-- 존재하는 foreign key 들의 constraints 들을 확인 할 수 있도록
select *
from INFORMATION_SCHEMA.TABLE_CONSTRAINTS
where CONSTRAINT_TYPE = 'FOREIGN KEY';


-- DECLARE @database nvarchar(50)
-- DECLARE @table nvarchar(50)

-- set @database = 'DatabaseName'
-- set @table = 'TableName'

-- DECLARE @sql nvarchar(255)
-- WHILE EXISTS(select * from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where constraint_catalog = @database and table_name = @table)
-- BEGIN
--     select    @sql = 'ALTER TABLE ' + @table + ' DROP CONSTRAINT ' + CONSTRAINT_NAME 
--     from    INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
--     where    constraint_catalog = @database and 
--             table_name = @table
--     exec    sp_executesql @sql
-- END