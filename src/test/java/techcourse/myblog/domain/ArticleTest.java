package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    @Test
    void replaceId_새로운_id_적용() {
        int previousId = 1;
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        Article article = new Article(previousId, title, coverUrl, contents);

        int newId = 2;
        Article expectedArticle = new Article(newId, title, coverUrl, contents);

        assertThat(article.replaceId(newId)).isEqualTo(expectedArticle);
    }

    @Test
    void isEqualToExceptId_동일한경우() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        Article article = new Article(1, title, coverUrl, contents);

        assertThat(article.isEqualToExceptId(article)).isTrue();
    }

    @Test
    void isEqualToExceptId_내용은_동일하고_다른_id() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        Article article = new Article(1, title, coverUrl, contents);
        Article articleWithDifferentId = new Article(2, title, coverUrl, contents);

        assertThat(article.isEqualToExceptId(articleWithDifferentId)).isTrue();
    }

    @Test
    void isEqualToExceptId_내용도_동일하지_않은_경우() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        Article article = new Article(1, title, coverUrl, contents);
        Article articleWithDifferentId = new Article(2, "different title", coverUrl, contents);

        assertThat(article.isEqualToExceptId(articleWithDifferentId)).isFalse();
    }
}