package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    @Test
    void creat_article() {
        assertThat(new Article("title", "contents", "coverUrl"))
                .isEqualTo(new Article("title", "contents", "coverUrl"));
    }
}