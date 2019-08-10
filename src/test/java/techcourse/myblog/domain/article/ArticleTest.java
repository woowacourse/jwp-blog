package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_FEATURE;
import static techcourse.myblog.utils.ArticleTestObjects.UPDATE_ARTICLE_FEATURE;
import static techcourse.myblog.utils.UserTestObjects.AUTHOR_DTO;

class ArticleTest {
    @Test
    void 게시글_정상_생성() {
        assertDoesNotThrow(() -> ARTICLE_FEATURE.toArticle(AUTHOR_DTO.toUser()));
    }

    @Test
    void 게시글_업데이트() {
        Article originalArticle = ARTICLE_FEATURE.toArticle(AUTHOR_DTO.toUser());
        Article editedArticle = UPDATE_ARTICLE_FEATURE.toArticle(AUTHOR_DTO.toUser());

        originalArticle.update(editedArticle);

        assertThat(originalArticle.getArticleFeature().getTitle()).isEqualTo(editedArticle.getArticleFeature().getTitle());
        assertThat(originalArticle.getArticleFeature().getCoverUrl()).isEqualTo(editedArticle.getArticleFeature().getCoverUrl());
        assertThat(originalArticle.getArticleFeature()).isEqualTo(editedArticle.getArticleFeature());
    }
}
