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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static int currentArticleId = 1;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "jaemok")
                        .with("coverUrl", "yuarel")
                        .with("contents", "naeyong"))
                .exchange();
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_작성_페이지_이동_테스트() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_작성_요청_후_리다이렉팅_테스트() {
        webTestClient.post().uri("/articles")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 게시물_조회_테스트() {
        String title = "jaemok";
        String contents = "naeyong";

        webTestClient.get().uri("/articles/" + currentArticleId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    //assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }

    @Test
    void 게시물_추가_요청_테스트() {
        String newTitle = "newTitle";
        String newCoverUrl = "newCoverUrl";
        String newContents = "newContents";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", newCoverUrl)
                        .with("contents", newContents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(response.getResponseHeaders().getLocation())
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(newTitle)).isTrue();
                                //assertThat(body.contains(updatedCoverUrl)).isTrue();
                                assertThat(body.contains(newContents)).isTrue();
                            });
                });
    }

    @Test
    void 게시물_수정_페이지_이동_테스트() {
        webTestClient.get().uri("/articles/" + currentArticleId + "/edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_수정_요청_테스트() {
        String updatedTitle = "updatedTitle";
        String updatedCoverUrl = "updatedCoverUrl";
        String updatedContents = "updatedContents";

//        webTestClient.put().uri("/articles/" + currentArticleId)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters
//                        .fromFormData("title", updatedTitle)
//                        .with("coverUrl", updatedCoverUrl)
//                        .with("contents", updatedContents))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .consumeWith(response -> {
//                    String body = new String(response.getResponseBody());
//                    assertThat(body.contains(updatedTitle)).isTrue();
//                    //assertThat(body.contains(updatedCoverUrl)).isTrue();
//                    assertThat(body.contains(updatedContents)).isTrue();
//                });

        webTestClient.put().uri("/articles/" + currentArticleId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", updatedTitle)
                        .with("coverUrl", updatedCoverUrl)
                        .with("contents", updatedContents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(response.getResponseHeaders().getLocation())
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(updatedTitle)).isTrue();
                                //assertThat(body.contains(updatedCoverUrl)).isTrue();
                                assertThat(body.contains(updatedContents)).isTrue();
                            });
                });
    }

    @AfterEach
    void 게시물_삭제_요청_테스트() {
        webTestClient.delete().uri("/articles/" + currentArticleId++)
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}
