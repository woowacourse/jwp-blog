package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.Utils.TestConstants.*;
import static techcourse.myblog.Utils.TestUtils.getBody;
import static techcourse.myblog.Utils.TestUtils.logInAsBaseUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void createComment() {
        webTestClient.post()
                .uri("/comment")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
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
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .body(BodyInserters
                        .fromFormData("comment", testComment)
                        .with("articleId", SAMPLE_ARTICLE_ID.toString()))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles/1.*");

        WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/articles/" + SAMPLE_ARTICLE_ID.toString())
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(testComment);
    }

    @Test
    void updateComment() {
        String updateComment = "update comment";

        webTestClient.put()
                .uri("/comment/" + SAMPLE_COMMENT_ID)
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .body(BodyInserters.fromFormData("comment", updateComment)
                        .with("articleId", SAMPLE_ARTICLE_ID.toString()))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles/" + SAMPLE_ARTICLE_ID + ".*");

        WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/articles/" + SAMPLE_ARTICLE_ID.toString())
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(updateComment);
    }

    @Test
    void deleteComment() {
        String deleteComment = "delete comment";

        webTestClient.delete()
                .uri("/comment/" + SAMPLE_DELETE_COMMENT_ID)
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles/" + SAMPLE_ARTICLE_ID + ".*");

        WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/articles/" + SAMPLE_ARTICLE_ID.toString())
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).doesNotContain(deleteComment);
    }
}