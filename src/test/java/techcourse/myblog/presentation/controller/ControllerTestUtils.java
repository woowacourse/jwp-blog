package techcourse.myblog.presentation.controller;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentRequestDto;
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

    public static void createComment(WebTestClient webTestClient, CommentRequestDto commentRequestDto, Long articleId, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/articles/" + articleId + "/writing" + sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentRequestDto), CommentRequestDto.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
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
