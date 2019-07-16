package techcourse.myblog.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    private String title = "title";
    private String coverUrl = "coverUrl";
    private String contents = "contents";
    private Article article = new Article(title,coverUrl,contents);

    @Test
    void saveArticle() {
        articleRepository.save(article);
        assertThat(articleRepository.existsById(article.getArticleId())).isTrue();
        articleRepository.delete(article);
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

        assertThat(articleRepository.findById(article.getArticleId()).get()).isEqualTo(article);

        articleRepository.delete(article);
    }


    @Test
    void updateArticle() {
        ArticleVO article2 = new ArticleVO("update title", "update coverUrl", "update contents");
        article.update(article2);

        assertThat(article.getTitle().equals(article2.getTitle()));
        assertThat(article.getCoverUrl().equals(article2.getCoverUrl()));
        assertThat(article.getContents().equals(article2.getContents()));

        articleRepository.delete(article);
    }

    @Test
    void deleteArticle() {
        articleRepository.save(article);
        articleRepository.delete(article);

        assertThat(articleRepository.existsById(article.getArticleId())).isFalse();
    }
}
