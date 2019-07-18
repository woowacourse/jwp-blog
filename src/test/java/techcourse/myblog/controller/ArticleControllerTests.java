package techcourse.myblog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static techcourse.myblog.web.ArticleController.ARTICLE_MAPPING_URL;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    Article article;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        article = Article.builder()
                .title("a")
                .contents("a")
                .coverUrl("a")
                .build();
        articleService.save(article);
    }

    @Test
    void createForm_call_isOk() {
        webTestClient.get().uri(ARTICLE_MAPPING_URL + "/writing")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void save_call_is3xxRedirect() {
        webTestClient.post().uri(ARTICLE_MAPPING_URL)
                .body(BodyInserters
                        .fromFormData("title", article.getTitle())
                        .with("coverUrl", article.getCoverUrl())
                        .with("contents", article.getContents())
                )
                .exchange()
                .expectStatus()
                .is3xxRedirection();

    }

    @Test
    void updataForm_call_isOk() {
        webTestClient.get().uri(ARTICLE_MAPPING_URL + "/1/edit")
                .exchange().expectStatus().isOk();
    }

    @Test
    void update_call_is3xxRedirect() {
        webTestClient.put().uri(ARTICLE_MAPPING_URL + "/1")
                .body(BodyInserters
                        .fromFormData("title", article.getTitle())
                        .with("coverUrl", article.getCoverUrl())
                        .with("contents", article.getContents())
                )
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void update_otherValueSave_true() {
        webTestClient.put().uri(ARTICLE_MAPPING_URL + "/1")
                .body(BodyInserters
                        .fromFormData("title", "edit")
                        .with("coverUrl", "edit")
                        .with("contents", "edit")
                )
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody().consumeWith(redirectResponse -> {
            webTestClient.get()
                    .uri(redirectResponse.getResponseHeaders().get("location").get(0))
                    .exchange()
                    .expectBody().consumeWith(response -> {
                String body = new String(response.getResponseBody());
                assertThat(body.contains("edit")).isTrue();
                assertThat(body.contains("edit")).isTrue();
                assertThat(body.contains("edit")).isTrue();

            });
        });
    }

    @Test
    void delete_call_is3xxRedirect() {
        webTestClient.delete().uri(ARTICLE_MAPPING_URL + "/1")
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }


}