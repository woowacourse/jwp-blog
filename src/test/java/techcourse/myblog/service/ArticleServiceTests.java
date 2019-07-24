package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.Article;
import techcourse.myblog.presentation.MainControllerTests;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTests {

    private static final Logger log = LoggerFactory.getLogger(MainControllerTests.class);

    private final ArticleService articleService;
    private Article article;
    private Article savedArticle;

    @Autowired
    public ArticleServiceTests(ArticleService articleService) {
        this.articleService = articleService;
    }

    @BeforeEach
    void setUp() {
        article = new Article("new Article title", "new Article contents", "new Article awesome Cover");
        savedArticle = articleService.save(article);
    }

    @Test
    void findAll_test() {
        List<Article> articles = articleService.findAll();
        System.out.println("size : " + articles.size());
        assertThat(articles.size()).isEqualTo(1);
    }

    @Test
    void findById_test() {
        articleService.save(article);
        Article foundArticle = articleService.findById(article.getId());
        assertThat(foundArticle).isEqualTo(article);
    }

    @AfterEach
    void tearDown() {
        articleService.deleteById(savedArticle.getId());
    }
}
