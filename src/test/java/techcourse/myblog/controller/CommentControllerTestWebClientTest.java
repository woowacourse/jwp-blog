package techcourse.myblog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTestWebClientTest extends MyblogApplicationTests {
    private WebTestClient webTestClient;
    private LoginControllerTest loginControllerTest;

    @Autowired
    private UserRepository userRepository;

    private String defaultCookie;
    private String testCookie;

    @Autowired
    public CommentControllerTestWebClientTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
        this.loginControllerTest = new LoginControllerTest(this.webTestClient, userRepository);
    }

    @BeforeEach
    void setUp() {
        defaultCookie = loginControllerTest.getLoginCookie(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD);
        testCookie = loginControllerTest.getLoginCookie(TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }

    @Test
    void comment_save() {
        String contents = "contents hahahah";

        webTestClient.post().uri("/comment").header("Cookie", defaultCookie)
                .body(BodyInserters
                        .fromFormData("contents", contents)
                        .with("articleId", "1"))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody().consumeWith(response -> {
            assertThat(response.getResponseHeaders().getLocation().toString().contains("articles")).isTrue();
        });
    }

    @Test
    void comment_save_미로그인시() {
        String contents = "contents hahahah";

        webTestClient.post().uri("/comment")
                .body(BodyInserters
                .fromFormData("contents", contents)
                .with("articleId", "1"))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody().consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isTrue();
        });
    }

    @Test
    void show_comment_edit_page() {
        webTestClient.get().uri("/comment/1/edit").header("Cookie", defaultCookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void show_comment_edit_page_비로그인() {
        webTestClient.get().uri("/comment/1/edit")
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void show_comment_edit_page_다른_사용자() {
        webTestClient.get().uri("/comment/1/edit").header("Cookie", testCookie)
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void comment_update() {
        String updateContents = "update";
        webTestClient.put().uri("/comment").header("Cookie", defaultCookie)
                .body(BodyInserters
                .fromFormData("contents", updateContents)
                .with("articleId","1")
                .with("id","1"))
                .exchange()
                .expectStatus()
                .isFound().expectBody().consumeWith(response -> {
                    webTestClient.get().uri("/articles/1").header("Cookie", defaultCookie)
                            .exchange()
                            .expectBody().consumeWith(getResponse -> {
                                String body = new String(getResponse.getResponseBody());
                        assertThat(body.contains(updateContents)).isTrue();
                        assertThat(body.contains(COMMENT_CONTENTS)).isFalse();

                        webTestClient.put().uri("/comment").header("Cookie", defaultCookie)
                                .body(BodyInserters
                                .fromFormData("contents", COMMENT_CONTENTS)
                                .with("articleId", "1")
                                .with("id", "1"))
                                .exchange()
                                .expectStatus().isFound();
                    });
        });
    }

    @Test
    void comment_update_비로그인() {
        String updateContents = "update";
        webTestClient.put().uri("/comment")
                .body(BodyInserters
                .fromFormData("contents", updateContents)
                .with("articleId", "1")
                .with("id", "1"))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isTrue();
                });
    }

    @Test
    void comment_update_다른사용자() {
        String updateContents = "update";
        webTestClient.put().uri("/comment").header("Cookie", testCookie)
                .body(BodyInserters
                        .fromFormData("contents", updateContents)
                        .with("articleId", "1")
                        .with("id", "1"))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("articles")).isFalse();
                });
    }

    @Test
    void comment_update_틀린게시글() {
        String updateContents = "update";
        webTestClient.put().uri("/comment").header("Cookie", testCookie)
                .body(BodyInserters
                        .fromFormData("contents", updateContents)
                        .with("articleId", "2")
                        .with("id", "1"))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("articles")).isFalse();
                });
    }

    @Test
    void comment_delete() {
        String deleteContents = "test contents";

        webTestClient.delete().uri("/comment/2").header("Cookie", defaultCookie)
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(innerResponse -> {
                    webTestClient.get().uri("/articles/1").header("Cookie", defaultCookie)
                            .exchange()
                            .expectBody()
                            .consumeWith(getResponse -> {
                                String body = new String(getResponse.getResponseBody());
                                assertThat(body.contains(deleteContents)).isFalse();
                            });
                });
    }

    @Test
    void comment_delete_비로그인() {
        webTestClient.delete().uri("/comment/1")
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isTrue();
                });
    }

    @Test
    void comment_delete_다른사용자() {
        webTestClient.delete().uri("/comment/1").header("Cookie", testCookie)
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("articles")).isFalse();
                });
    }
}
