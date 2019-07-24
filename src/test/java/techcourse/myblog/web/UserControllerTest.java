package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String USERNAME = "user";
    private static final String EMAIL = "user@mail.net";
    private static final String PASSWORD = "aSdF12#$";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String REGEX_SEMI_COLON = ";";
    private static final String REGEX_EQUAL = "=";

    private String jSessionId;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void signup() {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("username", USERNAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange();
    }

    EntityExchangeResult<byte[]> login() {
        return webTestClient
                .post().uri("/login")
                .body(BodyInserters
                        .fromFormData("username", USERNAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectBody()
                .returnResult();
    }

    String extractJSessionId(final EntityExchangeResult<byte[]> loginResult) {
        final String[] cookies = loginResult.getResponseHeaders().get(SET_COOKIE).stream()
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_SEMI_COLON);
        return Stream.of(cookies)
                .filter(it -> it.contains(JSESSIONID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(JSESSIONID + "가 없습니다."))
                .split(REGEX_EQUAL)[1]
                .trim();
    }

    @Test
    void 로그인() {
        final EntityExchangeResult<byte[]> response = login();
        jSessionId = extractJSessionId(response);
        assertThat(jSessionId).isNotBlank();
    }

    @Test
    void 잘못된_로그인_email() {
        webTestClient
                .post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", "a@a.com")
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void 잘못된_로그인_password() {
        webTestClient
                .post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", EMAIL)
                        .with("password", "qwerty"))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void 로그아웃() {
        final EntityExchangeResult<byte[]> loginResult = login();
        final EntityExchangeResult<byte[]> logoutResult = webTestClient
                .get().uri("/logout")
                .exchange().expectBody().returnResult();
        assertThat(extractJSessionId(loginResult))
                .isNotEqualTo(extractJSessionId(logoutResult));
    }
}