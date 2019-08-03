package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static techcourse.myblog.presentation.controller.ControllerTestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private String sessionId;

    @Test
    void 댓글_생성하기() {
        initalWork(createComment -> {
            webTestClient.post()
                    .uri("/articles/" + 1 + sessionId)
                    .body(BodyInserters.fromFormData("contents", "comment add test"))
                    .exchange()
                    .expectBody()
                    .consumeWith(result -> {
                        articleView(webTestClient, sessionId, 1L, articleView -> {
                            assertTrue(new String(articleView.getResponseBody()).contains("comment add test"));
                        });
                    });
        });
    }

    @Test
    void 댓글_수정하기() {
        CommentDto commentDto = new CommentDto();
        commentDto.setContents("comment test");

        initalWork(result -> {
            createComment(webTestClient, commentDto, 1L, sessionId, addComment -> {
                webTestClient.put()
                        .uri("/articles/3/comment-edit/1" + sessionId)
                        .body(BodyInserters.fromFormData("contents", "comment update"))
                        .exchange()
                        .expectBody().consumeWith(updateComment -> {
                    articleView(webTestClient, sessionId, 1L, commentView -> {
                        assertTrue(new String(commentView.getResponseBody()).contains("comment update"));
                    });
                });
            });
        });
    }

    @Test
    void 댓글_삭제하기() {
        CommentDto commentDto = new CommentDto();
        commentDto.setContents("comment test");
        initalWork(result -> {
            createComment(webTestClient, commentDto, 1L, sessionId, addComment -> {
                webTestClient.delete()
                        .uri("/articles/1/1" + sessionId)
                        .exchange()
                        .expectStatus().is3xxRedirection()
                        .expectBody()
                        .consumeWith(deleteComment -> {
                            articleView(webTestClient, sessionId, 1L, articleView -> {
                                assertFalse(new String(articleView.getResponseBody()).contains("comment test"));
                            });
                        });
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