package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleTest {
    @Test
    void 기본_백그라운드_URL() {
        Article article = new Article("title", "content", "");
        assertThat(article.getCoverUrl()).isEqualTo("/images/default/bg.jpg");
    }
}