INSERT INTO user(email, name, password) VALUES ('base@test.test', 'base', 'passWORD1!');
INSERT INTO user(email, name, password) VALUES ('update@test.test', 'update', 'passWORD1!');
INSERT INTO user(email, name, password) VALUES ('delete@test.test', 'delete', 'passWORD1!');
INSERT INTO user(email, name, password) VALUES ('mismatch@test.test', 'mismatch', 'passWORD1!');

INSERT INTO article(id, title, cover_url, contents, author_id) VALUES (1, 'title', 'cover url', 'contents', 1);

INSERT INTO comment(comment, author_id, article) VALUES ('test comment', 1, 1);
INSERT INTO comment(comment, author_id, article) VALUES ('delete comment', 1, 1);
