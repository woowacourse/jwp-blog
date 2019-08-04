package techcourse.myblog.presentation.controller;

import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;

import java.util.function.Consumer;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class ControllerTestUtils {

    public static String getSessionId(EntityExchangeResult<byte[]> postUserResponse) {
        return ";jsessionid=" + postUserResponse.getResponseCookies().getFirst("JSESSIONID").getValue();
    }

    public static void postUser(WebTestClient webTestClient, UserDto userDto, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/users")
                .body(BodyInserters.fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword()))
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void loginUser(WebTestClient webTestClient, LoginDto loginDto, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/login")
                .body(fromFormData("email", loginDto.getEmail())
                        .with("password", loginDto.getPassword()))
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void createArticle(WebTestClient webTestClient, ArticleDto articleDto, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/articles" + sessionId)
                .body(BodyInserters.fromFormData("title", articleDto.getTitle())
                        .with("coverUrl", articleDto.getCoverUrl())
                        .with("contents", articleDto.getContents()))
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void createComment(WebTestClient webTestClient, CommentDto commentDto, Long articleId, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/articles/" + articleId + sessionId)
                .body(BodyInserters.fromFormData("contents", commentDto.getContents()))
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void articleView(WebTestClient webTestClient, String sessionId, Long articleId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
                .uri("/articles/" + articleId + sessionId)
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }
}
