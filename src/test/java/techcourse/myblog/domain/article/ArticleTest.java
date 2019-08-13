package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_DTO;
import static techcourse.myblog.utils.ArticleTestObjects.UPDATE_ARTICLE_DTO;
import static techcourse.myblog.utils.UserTestObjects.AUTHOR_DTO;

class ArticleTest {
    @Test
    void 게시글_정상_생성() {
        assertDoesNotThrow(() -> ARTICLE_DTO.toArticle(AUTHOR_DTO.toUser()));
    }

    @Test
    void 게시글_업데이트() {
        Article originalArticle = ARTICLE_DTO.toArticle(AUTHOR_DTO.toUser());
        Article editedArticle = UPDATE_ARTICLE_DTO.toArticle(AUTHOR_DTO.toUser());

        originalArticle.update(editedArticle);

        assertThat(originalArticle.getTitle()).isEqualTo(editedArticle.getTitle());
        assertThat(originalArticle.getCoverUrl()).isEqualTo(editedArticle.getCoverUrl());
        assertThat(originalArticle.getContents()).isEqualTo(editedArticle.getContents());
    }
}