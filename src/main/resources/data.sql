INSERT INTO user(email, name, password, CREATED_DATE_TIME, LAST_MODIFIED_DATE_TIME)
VALUES ('test@test.test', 'test', 'passWORD1!', '2019-08-08 12:58:06.711', '2019-08-08 12:58:06.711');
INSERT INTO user(email, name, password, CREATED_DATE_TIME, LAST_MODIFIED_DATE_TIME)
VALUES ('update@test.test', 'name', 'passWORD1!', '2019-08-08 12:58:06.711', '2019-08-08 12:58:06.711');
INSERT INTO user(email, name, password, CREATED_DATE_TIME, LAST_MODIFIED_DATE_TIME)
VALUES ('delete@test.test', 'name', 'passWORD1!', '2019-08-08 12:58:06.711', '2019-08-08 12:58:06.711');
INSERT INTO user(email, name, password, CREATED_DATE_TIME, LAST_MODIFIED_DATE_TIME)
VALUES ('test2@test.test', 'test2', 'passWORD1!', '2019-08-08 12:58:06.711', '2019-08-08 12:58:06.711');

INSERT INTO article(title, cover_url, contents, author_id, CREATED_DATE_TIME, LAST_MODIFIED_DATE_TIME)
VALUES ('test2@test.test', 'test2', 'passWORD1!', 1, '2019-08-08 12:58:06.711', '2019-08-08 12:58:06.711');

INSERT INTO comment(comment, author_id, article_id, CREATED_DATE_TIME, LAST_MODIFIED_DATE_TIME)
VALUES ('test comment', 1, 1, '2019-08-08 12:58:06.711', '2019-08-08 12:58:06.711');