package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleVo;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTests {
    private Article article;

    @BeforeEach
    void setUp() {
        User author = new User("name", "test@test.test", "passWORD1!");
        ArticleVo articleVo = new ArticleVo("title", "url", "contents");
        article = new Article(author, articleVo);
    }

    @Test
    void updateArticle() {
        String newTitle = "new Title";
        String newCoverUrl = "new CoverUrl";
        String newContents = "new Contents";
        ArticleVo newArticleVo = new ArticleVo(newTitle, newCoverUrl, newContents);

        article.updateArticle(newArticleVo);

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getCoverUrl()).isEqualTo(newCoverUrl);
        assertThat(article.getContents()).isEqualTo(newContents);
    }
}
