package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ArticleTest {
    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article("test title", "test contents", "test cover url");
    }

    @Test
    void getCopyTest() {
        Article copy = article.getCopy();
        assertThat(article.getId()).isEqualTo(copy.getId());
        validateTwoArticlesHaveSameData(article, copy);
        assertThat(article == copy).isFalse();
    }

    @Test
    void updateTest() {
        Article updated = new Article("updated title", "updated contents", "updated cover url");
        article.update(updated);
        assertThat(article.getId()).isNotEqualTo(updated.getId());
        validateTwoArticlesHaveSameData(article, updated);
        assertThat(article == updated).isFalse();
    }

    @Test
    void idIncrementTest() {
        assertThat((new Article("new title", "new contents", "new cover url")).getId()).isGreaterThan(article.getId());
    }

    private void validateTwoArticlesHaveSameData(Article a1, Article a2) {
        assertThat(a1.getTitle()).isEqualTo(a2.getTitle());
        assertThat(a1.getContents()).isEqualTo(a2.getContents());
        assertThat(a1.getCoverUrl()).isEqualTo(a2.getCoverUrl());
    }
}
