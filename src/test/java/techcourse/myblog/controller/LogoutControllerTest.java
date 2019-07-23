package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogoutControllerTest {
    private static final String USER_NAME_1 = "test1";
    private static final String EMAIL_1 = "test1@test.com";
    private static final String PASSWORD_1 = "12345678";
    private static final String LOGIN_TEXT = "Login";

    private WebTestClient webTestClient;
    private String cookie;

    @Autowired
    public LogoutControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange();

        cookie = webTestClient.post().uri("login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    @Test
    void 로그아웃했을_시_메인에_로그인_문자가_뜨는지_확인() {
        webTestClient.get().uri("/logout").header("cookie", cookie).exchange();

        webTestClient.get().uri("/").header("cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(LOGIN_TEXT)).isTrue();
                });

    }

    @AfterEach
    void tearDown() {
        cookie = webTestClient.post().uri("login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", EMAIL_1)
                        .with("password", PASSWORD_1))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");

        webTestClient.delete().uri("/users").header("Cookie", cookie)
                .exchange();
    }
}