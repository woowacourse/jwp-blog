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

    private ArticleRepository articleRepository;
    private String title;
    private String coverUrl;
    private String contents;

    @BeforeEach
    void setup() {
        articleRepository = new ArticleRepository();
        title = "title";
        coverUrl = "coverUrl";
        contents = "contents";
    }


    @Test
    void saveArticle() {
        articleRepository.save(new Article(title, coverUrl, contents));
        assertThat(articleRepository.size()).isEqualTo(1);
    }


    @Test
    void findAllArticles() {
        Article article1 = new Article(title, coverUrl, contents);
        Article article2 = new Article("title2", "coverUrl2", "contents2");
        Article article3 = new Article("title3", "coverUrl3", "contents3");
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(article1,article2,article3));
    }


    @Test
    void findArticleById() {
        Article article  = new Article(title,coverUrl,contents);
        articleRepository.save(article);
        assertThat(articleRepository.findById(article.getArticleId())).isEqualTo(article);
    }


    @Test
    void updateArticle() {
        Article article  = new Article(title,coverUrl,contents);
        Article article2  = new Article("update title","update coverUrl","update contents");
        articleRepository.save(article);
        articleRepository.update(article,article2);
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(article2));
    }

    @Test
    void deleteArticle() {
        Article article  = new Article(title,coverUrl,contents);
        articleRepository.save(article);
        articleRepository.delete(article.getArticleId());
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList());
    }
}
