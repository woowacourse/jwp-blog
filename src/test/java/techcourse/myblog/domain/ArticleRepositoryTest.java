package techcourse.myblog.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class ArticleRepositoryTest {

    private ArticleRepository articleRepository;
    private String title;
    private String coverUrl;
    private String contents;
    private Article article;

    @BeforeEach
    void setup() {
        articleRepository = new ArticleRepository();
        title = "title";
        coverUrl = "coverUrl";
        contents = "contents";
        article = new Article(title, coverUrl, contents);
    }


    @Test
    void saveArticle() {
        articleRepository.save(article);
        assertThat(articleRepository.count()).isEqualTo(1);
    }


    @Test
    void findAllArticles() {
        Article article2 = new Article("title2", "coverUrl2", "contents2");
        Article article3 = new Article("title3", "coverUrl3", "contents3");
        articleRepository.save(article);
        articleRepository.save(article2);
        articleRepository.save(article3);

        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(article, article2, article3));
    }


    @Test
    void findArticleById() {
        articleRepository.save(article);
        assertThat(articleRepository.findById(article.getArticleId())).isEqualTo(article);
    }


    @Test
    void updateArticle() {
        ArticleVO article2 = new ArticleVO("update title", "update coverUrl", "update contents");
        articleRepository.save(article);
        articleRepository.update(article.getArticleId(), article2);

        assertThat(article.getTitle().equals(article2.getTitle()));
        assertThat(article.getCoverUrl().equals(article2.getCoverUrl()));
        assertThat(article.getContents().equals(article2.getContents()));
    }

    @Test
    void deleteArticle() {
        articleRepository.save(article);
        articleRepository.delete(article.getArticleId());

        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList());
    }
}
