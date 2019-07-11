package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {

    @Test
    void update() {
        Article article = Article.of("a","b","c");
        article.update(Article.of("1","2","3"));
        assertThat(article.getTitle()).isEqualTo("1");
        assertThat(article.getCoverUrl()).isEqualTo("2");
        assertThat(article.getContents()).isEqualTo("3");
    }
}
