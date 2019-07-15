package techcourse.myblog.web;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private static final int ARTICLE_ID = 0;

    @BeforeEach
    void 게시글_작성_확인() {
        webTestClient.post()
                .uri("/articles")
                .body(fromFormData("title", "test-title").with("coverUrl", "test-coverUrl").with("contents", "test-contents"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*articles.*");
    }

    @Test
    void index_페이지_조회() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글_작성_페이지_확인() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글_조회() {
        webTestClient.get()
                .uri("/articles/" + ARTICLE_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("test-title")).isTrue();
                    assertThat(body.contains("test-coverUrl")).isTrue();
                    assertThat(body.contains("test-contents")).isTrue();
                });
    }

    @Test
    void 게시글_수정_페이지_확인() {
        webTestClient.get()
                .uri("/articles/" + ARTICLE_ID)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글_수정_확인() {
        String newTitle = "newTitle";
        String newCoverUrl = "newUrl";
        String newContents = "newContents";
        webTestClient.put()
                .uri("/articles/" + ARTICLE_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("title", newTitle).with("coverUrl", newCoverUrl).with("contents", newContents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith((response -> {
                    webTestClient.get()
                            .uri(response.getRequestHeaders().getLocation())
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(response2 -> {
                                String body = new String(response2.getResponseBody());
                                assertThat(body.contains("newTitle")).isTrue();
                                assertThat(body.contains("newUrl")).isTrue();
                                assertThat(body.contains("newContents")).isTrue();
                            });
                }));
    }

    @AfterEach
    void 게시글_삭제_확인() {
        webTestClient.delete()
                .uri("/articles/" + ARTICLE_ID)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/");
    }
}
