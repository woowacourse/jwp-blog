insert into User(id, name, email, password) values(999, 'john', 'john123@example.com', 'p@ssW0rd');
insert into User(id, name, email, password) values(1000, 'paul', 'paul123@example.com', 'p@ssW0rd');
insert into Article(id, title, cover_url, contents, author_id) values(999, 'some article','', 'some contents', 999);
insert into Comment(id, contents, author_id, article_id) values(999, 'hello', 1000, 999);