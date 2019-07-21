package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest {
    static final String title = "제목";
    static final String coverUrl = "이미지";
    static final String contents = "내용";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void indexTest() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showWritingPageTest() {
        webTestClient.get().uri("/writing")
                    .exchange()
                    .expectStatus().isOk();
    }

    @Test
    void articleCreateReadTest() {
        webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("title", title)
                                .with("coverUrl", coverUrl)
                                .with("contents", contents)
                )
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(res ->
                        webTestClient.get().uri(res.getResponseHeaders().getLocation())
                                .exchange()
                                .expectBody()
                                .consumeWith(_res -> {
                                    String body = new String(_res.getResponseBody());
                                    assertTrue(body.contains(title));
                                    assertTrue(body.contains(coverUrl));
                                    assertTrue(body.contains(contents));
                                })
                );
    }

    @Test
    void articleUpdateTest() {
        final String updateTitle = "제목2";
        final String updateImage = "이미지2";
        final String updateContents = "내용2";

        webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("title", title)
                                    .with("coverUrl", coverUrl)
                                    .with("contents", contents)
                )
                .exchange()
                .expectBody()
                .consumeWith(res ->
                        webTestClient.put().uri(res.getResponseHeaders().getLocation())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(BodyInserters.fromFormData("title", updateTitle)
                                        .with("coverUrl", updateImage)
                                        .with("contents", updateContents))
                                .exchange()
                                .expectStatus().is3xxRedirection()
                                .expectBody()
                                .consumeWith(_res ->
                                        webTestClient.get().uri(res.getResponseHeaders().getLocation())
                                                .exchange()
                                                .expectBody()
                                                .consumeWith(__res -> {
                                                    String body = new String(__res.getResponseBody());
                                                    assertTrue(body.contains(title));
                                                    assertTrue(body.contains(coverUrl));
                                                    assertTrue(body.contains(contents));
                                                }))
                );
    }

    @Test
    void articleDelete() {
        webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("title", title)
                                .with("coverUrl", coverUrl)
                                .with("contents", contents)
                )
                .exchange()
                .expectBody()
                .consumeWith(res -> webTestClient.delete().uri(res.getResponseHeaders().getLocation())
                        .exchange()
                        .expectStatus().is3xxRedirection());
    }
}