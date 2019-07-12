package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        Article basicArticle = new Article("title", "url", "contents");
        articleRepository.save(basicArticle);
    }

    @Test
    void showArticles_Test() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm_Test() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void create_article_Test() {
        String title = "title";
        String coverUrl = "url";
        String contents = "abcde";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void writing_Test() {
        webTestClient.get()
                .uri("/writing")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void save_Article_Test() {
        Article article = new Article("a", "A", "a");
        articleRepository.save(article);

        webTestClient.get()
                .uri("/articles/" + article.getId())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void update_Article_Test() {
        Article expectedUpdatedArticle = new Article("b", "b", "b");
        articleRepository.updateArticleById(expectedUpdatedArticle, 1);

        Article updatedArticle = articleRepository.getArticleById(1);
        assertThat(updatedArticle.getTitle().equals("b"));
        assertThat(updatedArticle.getCoverUrl().equals("b"));
        assertThat(updatedArticle.getContents().equals("b"));
        webTestClient.get()
                .uri("/articles/" + 1)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void delete_Article_Test() {
        assertThrows(IllegalArgumentException.class, () -> articleRepository.removeArticleById(10));
    }

}
