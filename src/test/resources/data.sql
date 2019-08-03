insert into Blog_User(id, name, email, password) values(999, 'john', 'john123@example.com', 'p@ssW0rd');
insert into Blog_User(id, name, email, password) values(1000, 'paul', 'paul123@example.com', 'p@ssW0rd');
insert into Article(id, title, cover_url, contents, author_id) values(999, 'some article','', 'some contents', 999);
insert into Comment(id, contents, author_id, article_id) values(999, 'hello1', 1000, 999);
-- Will be used at test techcourse.myblog.web.CommentControllerTests.댓글작성자가_댓글_수정()
insert into Comment(id, contents, author_id, article_id) values(1000, 'hello2', 1000, 999);
-- Will be used at test techcourse.myblog.web.CommentControllerTests.댓글작성자가_댓글_삭제()
insert into Comment(id, contents, author_id, article_id) values(1001, 'hello3', 1000, 999);