package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;
    private String title;
    private String coverUrl;
    private String contents;

    @BeforeEach
    void setUp() {
        title = "title";
        coverUrl = "";
        contents = "contents";

        webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 메인화면() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글_생성_페이지_이동() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글_생성() {
        webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri("/articles/2")
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

    @Test
    void 게시글_조회() {
        title = "title";
        coverUrl = "";
        contents = "contents";

        EntityExchangeResult result = webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().returnResult();

        webTestClient.get().uri(result.getResponseHeaders().getLocation())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("title")).isTrue();
                    assertThat(body.contains("")).isTrue();
                    assertThat(body.contains("contents")).isTrue();
                });
    }

    @Test
    void 게시글_수정() {

        EntityExchangeResult result = webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().returnResult();

        String title = "newTitle";
        String coverUrl = "";
        String contents = "contents";

        webTestClient.put().uri(result.getResponseHeaders().getLocation())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri(response.getResponseHeaders().getLocation())
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

    @Test
    void 게시글_삭제() {
        webTestClient.delete().uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get().uri("/articles/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void 게시글_수정_페이지_이동() {
        webTestClient.get().uri("/articles/1/edit")
                .exchange()
                .expectStatus().isOk();
    }
}
