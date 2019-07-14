package techcourse.myblog.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.exception.IllegalArticleArgumentsException;
import techcourse.myblog.web.ArticleDTO;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleTest {
    private static final int TEST_ARTICLE_ID = 1;
    private ArticleDTO articleDTO;

    @Test
    @DisplayName("Article의 title이 null인 경우 예외를 던진다.")
    void checkNullTitleTest() {
        articleDTO = new ArticleDTO(
                null,
                "not null",
                "not null"
        );

        assertThatThrownBy(() -> Article.of(TEST_ARTICLE_ID, articleDTO))
                .isInstanceOf(IllegalArticleArgumentsException.class);
    }

    @Test
    @DisplayName("Article의 coverUrl이 null인 경우 예외를 던진다.")
    void checkNullConverUrlTest() {
        articleDTO = new ArticleDTO(
                "not null",
                null,
                "not null"
        );

        assertThatThrownBy(() -> Article.of(TEST_ARTICLE_ID, articleDTO))
                .isInstanceOf(IllegalArticleArgumentsException.class);
    }

    @Test
    @DisplayName("Article의 contents가 null인 경우 예외를 던진다.")
    void checkNullContentsTest() {
        articleDTO = new ArticleDTO(
                "not null",
                "not null",
                null
        );

        assertThatThrownBy(() -> Article.of(TEST_ARTICLE_ID, articleDTO))
                .isInstanceOf(IllegalArticleArgumentsException.class);
    }
}