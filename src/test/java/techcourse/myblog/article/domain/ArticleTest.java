package techcourse.myblog.article.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.data.ArticleDataForTest;
import techcourse.myblog.data.UserDataForTest;
import techcourse.myblog.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticleTest {
    private final User author = User.builder()
            .id(1)
            .email(UserDataForTest.USER_EMAIL)
            .password(UserDataForTest.USER_PASSWORD)
            .name(UserDataForTest.USER_NAME)
            .build();

    private final Article article = Article.builder()
            .id(1)
            .contents(ArticleDataForTest.ARTICLE_CONTENTS)
            .coverUrl(ArticleDataForTest.ARTICLE_COVER_URL)
            .title(ArticleDataForTest.ARTICLE_TITLE)
            .author(author)
            .build();

    @Test
    void 업데이트() {
        article.update("Update", "Update CoverUrl", "Update Contents");
        assertThat(article.getTitle()).isEqualTo("Update");
        assertThat(article.getCoverUrl()).isEqualTo("Update CoverUrl");
        assertThat(article.getContents()).isEqualTo("Update Contents");
    }

    @Test
    void notMatchAuthorId() {
        assertTrue(article.notMatchAuthorId(2));
    }
}
