package techcourse.myblog.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
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
        assertThat(articleRepository.findById(article.getArticleId())).isEqualTo(article);
    }


    @Test
    void update() {
        Article article  = new Article("title","coverUrl","contents");
        Article article2  = new Article("title2","coverUrl2","contents2");
        articleRepository.save(article);
        articleRepository.update(article,article2);
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(article2));
    }

    @Test
    void delete() {
        Article article  = new Article("title","coverUrl","contents");
        articleRepository.save(article);
        articleRepository.delete(article.getArticleId());
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList());
    }
}
