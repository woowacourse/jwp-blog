package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.CommentDto;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests extends BasicControllerTests {
    @Test
    void create_ajax_comment_test() {
        registerUser();
        sessionId = logInAndGetSessionId();
        result = writeArticle(sessionId);
        articleUri = result.getResponseHeaders().getLocation().getPath();
        System.out.println(">>> " + articleUri);
        CommentDto commentDto = new CommentDto(null,"abcd", null, 2L,  null);
        webTestClient.post().uri(articleUri + "/comments")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo("abcd")
                .jsonPath("$.articleId").isEqualTo("2");
    }

    @Test
    void update_ajax_comment_test() {
        registerUser();
        sessionId = logInAndGetSessionId();
        result = writeArticle(sessionId);
        articleUri = result.getResponseHeaders().getLocation().getPath();

        CommentDto commentDto = new CommentDto(3L,"abcd", null, 2L,  null);
        webTestClient.post().uri(articleUri + "/comments")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        CommentDto expectUpdateCommentDto = new CommentDto(3L,"kangmin",null, 2L, null);
        System.out.println(articleUri);
        webTestClient.put().uri(articleUri + "/comments/3")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(expectUpdateCommentDto), CommentDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo("kangmin")
                .jsonPath("$.articleId").isEqualTo("2");
    }

    @Test
    void delete_ajax_comment_test() {
        registerUser();
        sessionId = logInAndGetSessionId();
        result = writeArticle(sessionId);
        articleUri = result.getResponseHeaders().getLocation().getPath();

        CommentDto commentDto = new CommentDto(null,"abcd", null, 2L,  null);
        webTestClient.post().uri(articleUri + "/comments")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo("abcd")
                .jsonPath("$.articleId").isEqualTo("2");

        webTestClient.delete().uri(articleUri+"/comments/3")
                .header("Cookie",sessionId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus()
                .isOk();
    }
}