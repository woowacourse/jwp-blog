package techcourse.myblog.articles;

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

    @Test
    void javaBean() {
        Article article = new Article();
        String contents = "contents";
        String title = "title";
        String coverUrl = "coverUrl";
//
        article.setContents(contents);
        article.setTitle(title);
        article.setCoverUrl(coverUrl);

        assertThat(article.getContents()).isEqualTo(contents);
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getCoverUrl()).isEqualTo(coverUrl);
    }
}
