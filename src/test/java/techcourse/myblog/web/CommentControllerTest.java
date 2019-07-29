package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    private static final Long SAMPLE_ARTICLE_ID = 1L;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void createComment() {
        webTestClient.post()
                .uri("/comment")
                .cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
                .body(BodyInserters
                        .fromFormData("comment", "testcomment")
                        .with("articleId", SAMPLE_ARTICLE_ID.toString()))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles/1.*");
    }

    @Test
    void showCommentsInArticle() {
        String testComment = "test comment";

        webTestClient.post()
                .uri("/comment")
                .cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
                .body(BodyInserters
                        .fromFormData("comment", testComment)
                        .with("articleId", SAMPLE_ARTICLE_ID.toString()))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles/1.*");

        webTestClient.get().uri("/articles/" + SAMPLE_ARTICLE_ID.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(testComment)).isTrue();
                });
    }
}