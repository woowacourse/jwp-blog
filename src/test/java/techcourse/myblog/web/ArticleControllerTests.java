package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private Article basicArticle;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        basicArticle = new Article("title", "url", "contents");
        articleRepository.save(basicArticle);
    }

    @Test
    void show_Article_Test() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writing_Article_Test() {
        webTestClient.get().uri("/writing")
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
    void add_Article_Test(){
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
                .body(BodyInserters
                        .fromFormData("title",basicArticle.getTitle())
                        .with("coverUrl",basicArticle.getCoverUrl())
                        .with("contents",basicArticle.getContents()))
                .exchange()
                .expectStatus()
                .is3xxRedirection().expectBody().consumeWith(redirectResponse -> {
                    webTestClient.get()
                            .uri(redirectResponse.getResponseHeaders().get("location").get(0))
                            .exchange()
                            .expectBody().consumeWith(response -> {
                                String body = new String(response.getResponseBody());
                        assertThat(body.contains("title")).isTrue();
                        assertThat(body.contains("url")).isTrue();
                        assertThat(body.contains("contents")).isTrue();
                    });
        });
    }

    @Test
    void save_Article_Test() {
        webTestClient.get()
                .uri("/articles/" + basicArticle.getId())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void edit_Article_Test() {
        webTestClient.get()
                .uri("/articles/" + basicArticle.getId()+"/edit")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void update_Article_Test() {
        Article updatedArticle = new Article("b", "b", "b");
        articleRepository.updateArticleById(updatedArticle, 1);

        webTestClient.put()
                .uri("/articles/1")
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
                .body(BodyInserters
                        .fromFormData("title",updatedArticle.getTitle())
                        .with("coverUrl",updatedArticle.getCoverUrl())
                        .with("contents",updatedArticle.getContents()))
                .exchange()
                .expectStatus()
                .is3xxRedirection().expectBody().consumeWith(redirectResponse -> {
            webTestClient.get()
                    .uri(redirectResponse.getResponseHeaders().get("location").get(0))
                    .exchange()
                    .expectBody().consumeWith(response -> {
                String body = new String(response.getResponseBody());
                assertThat(body.contains("b")).isTrue();
                assertThat(body.contains("b")).isTrue();
                assertThat(body.contains("b")).isTrue();
            });
        });
    }

    @Test
    void delete_Article_Test() {
        articleRepository.deleteAll();
        webTestClient.get()
                .uri("articles/1")
                .exchange()
                .expectStatus()
                .is5xxServerError();

    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }
}
