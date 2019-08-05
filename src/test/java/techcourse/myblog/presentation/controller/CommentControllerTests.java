package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.CommentJsonDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests extends BasicControllerTests {

    @Test
    void new_comment_test() {
        prepareTestForComment();
        String checkCondition = "444";

        checkTestPass(checkCondition, true);

        finishTestForComment();
    }

    @Test
    void delete_comment_test() {
        prepareTestForComment();

        webTestClient.delete().uri(articleUri+"/comments/" + ID)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
        checkTestPass("444", false);
        finishTestForComment();
    }

    @Test
    void update_comment_test() {
        prepareTestForComment();

        webTestClient.put().uri(articleUri+"/comments/" + ID)
                .header("Cookie", sessionId)
                .body(BodyInserters.fromFormData("contents", "7788"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        checkTestPass("7788", true);
        finishTestForComment();
    }


    @Test
    void update_comment_test_when_not_logged_in() {
        prepareTestForComment();

        webTestClient.get().uri("/logout")
                .header("Cookie", sessionId)
                .exchange();

        webTestClient.put().uri(articleUri+"/comments/" + ID)
                .header("Cookie", sessionId)
                .body(BodyInserters.fromFormData("contents", "7788"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        checkTestPass("7788", false);
        finishTestForComment();
    }

    @Test
    void delete_comment_when_not_logged_in() {
        prepareTestForComment();

        webTestClient.get().uri("/logout")
                .header("Cookie", sessionId)
                .exchange();

        webTestClient.delete().uri(articleUri+"/comments/" + ID)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().is3xxRedirection();

        checkTestPass("444", true);
        finishTestForComment();
    }

    @Test
    void update_when_different_person_login() {
        prepareTestForComment();
        sessionId = logInAndGetSessionIdDiff();

        webTestClient.put().uri(articleUri+"/comments/" + ID)
                .header("Cookie", sessionId)
                .body(BodyInserters.fromFormData("contents", "7788"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        checkTestPass("7788", false);
        finishTestForComment();
    }

    @Test
    void create_comment_test() {
        registerUser();
        sessionId = logInAndGetSessionId();
        result = writeArticle(sessionId);
        articleUri = result.getResponseHeaders().getLocation().getPath();
        CommentJsonDto commentJsonDto = new CommentJsonDto("hard@gmail.com", "777", 2L);
        webTestClient.post().uri(articleUri + "/jsoncomments")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentJsonDto), CommentJsonDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.email").isEqualTo("hard@gmail.com")
                .jsonPath("$.contents").isEqualTo("777")
                .jsonPath("$.articleId").isEqualTo("2");
    }
}