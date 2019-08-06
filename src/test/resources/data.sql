INSERT INTO user(user_id, user_name, email, password) VALUES (9, 'aiden', 'aiden@woowa.com', '12Woowa@@');
INSERT INTO user(user_id, user_name, email, password) VALUES (7, 'woowa', 'woowa@woowa.com', '12Woowa@@');
INSERT INTO article(id, contents, cover_url, title, user_id) VALUES (9, 'woowaTech', 'url', 'intro', 9);
INSERT INTO comment(id, contents, article_id, user_id) VALUES (19, 'comment contents', 9, 9);
INSERT INTO comment(id, contents, article_id, user_id) VALUES (20, 'comment contents', 9, 9);