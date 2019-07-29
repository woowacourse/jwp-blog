package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ArticleTest {

    User author = new User("abc", "eee@main.com", "author123!A@");

    @Test
    void 게시글_정상_생성() {
        assertDoesNotThrow(() ->
                new Article("testTitle", "testUrl", "testContents", author));
    }

    @Test
    void 게시글_업데이트() {
        Article editedArticle = new Article("수정됨", "수정됨", "수정됨", author);
        Article originalArticle = new Article("원본", "원본", "원본", author);

        originalArticle.update(editedArticle);

        assertThat(originalArticle.getTitle()).isEqualTo(editedArticle.getTitle());
        assertThat(originalArticle.getCoverUrl()).isEqualTo(editedArticle.getCoverUrl());
        assertThat(originalArticle.getContents()).isEqualTo(editedArticle.getContents());
    }
}