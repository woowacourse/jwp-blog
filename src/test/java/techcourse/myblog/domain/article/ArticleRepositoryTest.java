package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void save() {
        Article article = new Article(1, "title", "test", "test", 1);

        assertThat(articleRepository.save(article)).isEqualTo(article);
    }

}