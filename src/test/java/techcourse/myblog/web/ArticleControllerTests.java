package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {

    String title;
    String coverUrl;
    String contents;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        title = "titleTest";
        coverUrl = "urlTest";
        contents = "contentsTest";
    }

    @Test
    void article_form_page_status() {
        webTestClient.get().uri("/articles/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void create_Article_redirect() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*articles.*");
    }

    @Test
    void article_view_page_status() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri("/articles/0")
                            .exchange()
                            .expectStatus().isOk();
                });
    }

    @Test
    void article_edit_page_status() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri("/articles/0/edit")
                            .exchange()
                            .expectStatus().isOk();
                });
    }

    @Test
    void create_article_en() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri(response.getRequestHeaders().getLocation())
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(response2 -> {
                                String body = new String(response2.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });
                });
    }
}
