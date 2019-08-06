INSERT INTO USER(email, password, name) VALUES ('email@gmail.com', 'password1234!', 'name');
INSERT INTO USER(email, password, name) VALUES ('email2@gmail.com', 'password1234!', 'name');

INSERT INTO ARTICLE(title, cover_url, contents, author_id) VALUES ('article_title', 'article_cover_url', 'article_contents', 1);
INSERT INTO ARTICLE(title, cover_url, contents, author_id) VALUES ('article_title2', 'article_cover_url2', 'article_contents2', 2);

INSERT INTO COMMENT(contents, author_id, article_id) VALUES ('쏘쏘 멋져', 1, 1);
INSERT INTO COMMENT(contents, author_id, article_id) VALUES ('쏘쏘 멋져2', 2, 2);