INSERT INTO user (id, email, name, password)
VALUES (1, 'test1@gmail.com', 'testerOne', 'Aa12345!');
INSERT INTO user (id, email, name, password)
VALUES (2, 'test2@gmail.com', 'testerTwo', 'Aa12345!');
INSERT INTO article (id, contents, cover_url, title, author_id)
VALUES (1, 'Test Article Contents 1', 'Test Article CoverUrl 1', 'Test Article Title 1', 1);
INSERT INTO article (id, contents, cover_url, title, author_id)
VALUES (2, 'Test Article Contents 2', 'Test Article CoverUrl 2', 'Test Article Title 2', 1);
INSERT INTO comment (id, contents, article_id, author_id)
VALUES (1, 'Test Comment Contents 1', 1, 1);
INSERT INTO comment (id, contents, article_id, author_id)
VALUES (2, 'Test Comment Contents 2', 1, 1);