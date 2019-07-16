package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {
    @Test
    void 아이디_테스트() {
        Article article = new Article("제목", "URL", "내용");
        article.setId(3);
        assertThat(article.checkId(3)).isTrue();
    }
}