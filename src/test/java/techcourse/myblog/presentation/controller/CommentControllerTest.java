package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentRequestDto;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;

import java.util.function.Consumer;

import static techcourse.myblog.presentation.controller.ControllerTestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private String sessionId;

    @Test
    void 댓글_생성하기() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setId(1);
        commentRequestDto.setContents("testCommentContents");

        initalWork(createComment -> {
            webTestClient.post()
                    .uri("/articles/" + 1 + "/writing" + sessionId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .expectBody()
                    .jsonPath("$.contents").isEqualTo("testCommentContents");
        });
    }

    @Test
    void 댓글_수정하기() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setId(1);
        commentRequestDto.setContents("testCommentContents");

        CommentRequestDto commentEditRequestDto = new CommentRequestDto();
        commentEditRequestDto.setId(1);
        commentEditRequestDto.setContents("editCommentContents");

        initalWork(result -> {
            createComment(webTestClient, commentRequestDto, 1L, sessionId, addComment -> {
                webTestClient.put()
                        .uri("/articles/1/comment-edit/" + commentRequestDto.getId() + sessionId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .body(Mono.just(commentEditRequestDto), CommentRequestDto.class)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                        .expectBody()
                        .jsonPath("$.contents").isEqualTo("editCommentContents");
            });
        });
    }

    @Test
    void 댓글_삭제하기() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setId(1);
        commentRequestDto.setContents("testCommentContents");

        initalWork(result -> {
            createComment(webTestClient, commentRequestDto, 1L, sessionId, addComment -> {
                webTestClient.delete()
                        .uri("/articles/1/" + commentRequestDto.getId() + sessionId)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                        .expectBody()
                        .jsonPath("$.id").isEqualTo(commentRequestDto.getId());
            });
        });
    }

    private void initalWork(Consumer<EntityExchangeResult<byte[]>> result) {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setName("test");
        userDto.setPassword("password");

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(userDto.getEmail());
        loginDto.setPassword(userDto.getPassword());

        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("title");
        articleDto.setCoverUrl("coverUrl");
        articleDto.setContents("contetnts");

        postUser(webTestClient, userDto, postResponseUser -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                sessionId = getSessionId(postResponseLogin);
                createArticle(webTestClient, articleDto, sessionId, result);
            });
        });
    }
}