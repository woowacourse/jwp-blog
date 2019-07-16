package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {
    @Test
    void builder() {
        Article article = Article.builder()
                .title("title")
                .contents("contents")
                .coverUrl("coverUrl")
                .build();

        assertThat(article).isNotNull();
    }

}
