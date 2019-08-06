package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ArticleTest {

    @Test
    void 게시글_정상_생성() {
        assertDoesNotThrow(() ->
                new Article("testTitle", "testUrl", "testContents"));
    }

    @Test
    void 게시글_업데이트() {
        Article editedArticle = new Article("수정됨", "수정됨", "수정됨");
        Article originalArticle = new Article("원본", "원본", "원본");

        originalArticle.update(editedArticle);

        assertThat(originalArticle.getTitle()).isEqualTo(editedArticle.getTitle());
        assertThat(originalArticle.getCoverUrl()).isEqualTo(editedArticle.getCoverUrl());
        assertThat(originalArticle.getContents()).isEqualTo(editedArticle.getContents());
    }
}