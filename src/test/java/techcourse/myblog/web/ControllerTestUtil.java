package techcourse.myblog.web;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.LoginRequest;

public class ControllerTestUtil {
    public static final String NAME = "ethan";
    public static final String EMAIL = "sloth@sloth.com";
    public static final String PASSWORD = "Password123!";
    public static final String TITLE = "googler bmo";
    public static final String COVER_URL = "bmo.jpg";
    public static final String CONTENTS = "why bmo so great?";

    public static void signUp(WebTestClient webTestClient, String name, String email, String password) {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
        ;
    }

    public static String login(WebTestClient webTestClient, String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(request), LoginRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    public static void writeArticle(WebTestClient webTestClient, String title, String coverUrl, String contents,
                                    String cookie) {
        webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
        ;
    }
}
