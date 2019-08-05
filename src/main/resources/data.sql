INSERT INTO user(email, name, password) VALUES ('test@test.test', 'test', 'passWORD1!');
INSERT INTO user(email, name, password) VALUES ('update@test.test', 'name', 'passWORD1!');
INSERT INTO user(email, name, password) VALUES ('delete@test.test', 'name', 'passWORD1!');
INSERT INTO user(email, name, password) VALUES ('test2@test.test', 'test2', 'passWORD1!');

INSERT INTO article(title, cover_url, contents, author_id) VALUES ('test2@test.test', 'test2', 'passWORD1!', 1);

INSERT INTO comment(comment, author_id, article_id) VALUES ('test comment', 1, 1);
