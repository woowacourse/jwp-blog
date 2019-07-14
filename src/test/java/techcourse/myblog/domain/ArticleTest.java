package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

class ArticleTest {

    @Test
    public void title_null_exeption() {
        assertThatThrownBy(() -> {
            new Article(0, "", "url", "contents");
        }).isInstanceOf(NullArticleElementException.class);
    }

    @Test
    public void contents_null_exeption() {
        assertThatThrownBy(() -> {
            new Article(0, "title", "url", "");
        }).isInstanceOf(NullArticleElementException.class);
    }

    @Test
    public void url_null_exeption() {
        assertThatThrownBy(() -> {
            new Article(0, "title", "", "contents");
        }).isInstanceOf(NullArticleElementException.class);
    }
}