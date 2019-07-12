package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryTest {
    @Test
    void saveTest() {
        ArticleRepository articleRepository = new ArticleRepository();
        Article article = articleRepository.save(new Article("title", "contents", "coverUrl"));
        assertThat(articleRepository.findAll().get(0)).isEqualTo(article);
    }
}