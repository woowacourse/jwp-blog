package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleRepositoryTest {

    @Test
    void findAll() {
    }

    @Test
    void save() {
        ArticleRepository articleRepository = new ArticleRepository();
        Article article = new Article("title", "contents", "coverUrl");
        List<Article> articles = new ArrayList<>();
        articles.add(article);
        articleRepository.save(article);

        assertThat(articleRepository.findAll()).isEqualTo(articles);
    }
}