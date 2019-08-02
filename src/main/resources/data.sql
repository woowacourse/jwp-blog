insert into user(id, email, password, name) values (1, 'test@test.com', '123123123', '미스터코');
insert into user(id, email, password, name) values (2, 'ok94life@gmail.com', '123123123', '유니');

insert into article(id, contents, category_id, cover_url, title, author) values (1, 'testtest', 1, 'https://img.com', 'TEST', 1);
insert into article(id, contents, category_id, cover_url, title, author) values (2, 'sssssss', 1, '', 'test2', 1);
insert into article(id, contents, category_id, cover_url, title, author) values (3, 'sssssss', 2, '', 'test3', 1);

insert into comment(id, contents, article, author) values (1, 'sssss', 1, 1);
insert into comment(id, contents, article, author) values (2, 'sssss', 1, 1);

insert into category(id, name) values (1, 'java');