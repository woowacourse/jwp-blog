package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static final String APPENDING_DELETE_MESSAGE = "will be deleted";

    @Autowired
    private WebTestClient webTestClient;

    private final String title = "안녕슬로스";
    private final String contents = "쩌싀워더펑여우";
    private final String coverUrl = "/images/article/sloth.jpg";

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 새로운_게시글_추가_테스트() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 게시물_작성_테스트() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(response.getResponseHeaders().getLocation())
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });
                });
    }

    @Test
    void 수정할_게시물의_내용_출력_확인_테스트() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri(response.getResponseHeaders().getLocation() + "/edit")
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });
                });
    }

    @Test
    void 게시글_삭제_테스트() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title + APPENDING_DELETE_MESSAGE)
                        .with("coverUrl", coverUrl + APPENDING_DELETE_MESSAGE)
                        .with("contents", contents + APPENDING_DELETE_MESSAGE))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.delete()
                            .uri(response.getResponseHeaders().getLocation())
                            .exchange()
                            .expectStatus()
                            .isFound()
                            .expectBody()
                            .consumeWith(
                                    res -> {
                                        webTestClient.get()
                                                .uri(res.getResponseHeaders().getLocation())
                                                .exchange()
                                                .expectStatus()
                                                .isOk()
                                                .expectBody()
                                                .consumeWith(r -> {
                                                    String body = new String(r.getResponseBody());
                                                    assertThat(body.contains(title + APPENDING_DELETE_MESSAGE)).isFalse();
                                                    assertThat(body.contains(contents + APPENDING_DELETE_MESSAGE)).isFalse();
                                                    assertThat(body.contains(coverUrl + APPENDING_DELETE_MESSAGE)).isFalse();
                                                });
                                    });
                });
    }
}
