package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 메인화면_조회() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 글쓰기_폼_열기() {
        webTestClient.get().uri("/articles")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_생성() {
        String title = "제목";
        String coverUrl = "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg";
        String contents = "CONTENTS";

        webTestClient.post().uri("/articles")
                .body(
                        BodyInserters
                                .fromFormData("title", title)
                                .with("coverUrl", coverUrl)
                                .with("contents", contents)
                ).exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(
                        response -> {
                            String url = response.getResponseHeaders().get("Location").get(0);
                            webTestClient.get().uri(url)
                                    .exchange()
                                    .expectStatus().isOk()
                                    .expectBody()
                                    .consumeWith(
                                            redirectResponse -> {
                                                String body = new String(redirectResponse.getResponseBody());
                                                assertThat(body.contains(title)).isTrue();
                                                assertThat(body.contains(coverUrl)).isTrue();
                                                assertThat(body.contains(contents)).isTrue();
                                            }
                                    );
                        }
                );
    }

    @Test
    void 게시글_수정() {
        String title = "제목";
        String coverUrl = "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg";
        String contents = "CONTENTS";

        webTestClient.post().uri("/articles")
                .body(
                        BodyInserters
                                .fromFormData("title", "잘가")
                                .with("coverUrl", "")
                                .with("contents", "Bye")
                ).exchange()
                .expectBody()
                .consumeWith(
                        response -> {
                            Long id = Long.parseLong(response.getResponseHeaders().get("Location").get(0).split("/")[4]);
                            webTestClient.put().uri("/articles/" + id)
                                    .body(
                                            BodyInserters
                                                    .fromFormData("title", title)
                                                    .with("coverUrl", coverUrl)
                                                    .with("contents", contents)
                                    ).exchange()
                                    .expectStatus().is3xxRedirection()
                                    .expectBody()
                                    .consumeWith(
                                            response1 -> {
                                                String url = response1.getResponseHeaders().get("Location").get(0);
                                                webTestClient.get().uri(url)
                                                        .exchange()
                                                        .expectStatus().isOk()
                                                        .expectBody()
                                                        .consumeWith(
                                                                redirectResponse -> {
                                                                    String body = new String(redirectResponse.getResponseBody());
                                                                    assertThat(body.contains(title)).isTrue();
                                                                    assertThat(body.contains(coverUrl)).isTrue();
                                                                    assertThat(body.contains(contents)).isTrue();
                                                                }
                                                        );
                                            }
                                    );
                        }
                );

    }

    @Test
    void 게시글_삭제() {
        webTestClient.post().uri("/articles")
                .body(
                        BodyInserters
                                .fromFormData("title", "잘가")
                                .with("coverUrl", "")
                                .with("contents", "Bye")
                ).exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    Long id = Long.parseLong(response.getResponseHeaders().get("Location").get(0).split("/")[4]);
                    webTestClient.delete().uri("/articles/" + id)
                            .exchange()
                            .expectStatus().is3xxRedirection();
                });


    }
}
