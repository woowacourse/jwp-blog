package techcourse.myblog.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryTest {

    ArticleRepository articleRepository;

    @BeforeEach
    void setup() {
        articleRepository = new ArticleRepository();
    }

    @Test
    void save() {
        articleRepository.save(new Article("title", "coverUrl", "contents"));
        assertThat(articleRepository.size()).isEqualTo(1);
    }

    @Test
    void findAll() {
        Article article1 = new Article("title", "coverUrl", "contents");
        Article article2 = new Article("title", "coverUrl", "contents");
        Article article3 = new Article("title", "coverUrl", "contents");
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(article1,article2,article3));
    }

    @Test
    void findById() {
        Article article  = new Article("title","coverUrl","contents");
        articleRepository.save(article);
        assertThat(articleRepository.findById(1L)).isEqualTo(article);
    }
}
