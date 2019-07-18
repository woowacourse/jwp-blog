package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;


@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String SIGN_UP_PAGE = "/users/signup";
    private static final String LOGIN_PAGE = "/users/login";
    private static final String USER_NAME = "test1";
    private static final String EMAIL = "test1@test.com";
    private static final String WRONG_EMAIL = "test2@test.com";
    private static final String PASSWORD = "1234";
    private static final String WRONG_PASSWORD = "12345";

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange();
    }

    @Test
    void 로그인_폼_테스트() {
        webTestClient.get().uri("/users/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 유저_생성_응답_테스트() {

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    webTestClient.get().uri(url)
                            .exchange()
                            .expectStatus().isOk();
                });
    }

    @Test
    void 유저_조회_테스트() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = getResponseBody(response.getResponseBody());
                    assertThat(body.contains(USER_NAME)).isTrue();
                    assertThat(body.contains(EMAIL)).isTrue();
                });
    }

    @Test
    void 유저_이메일_중복_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME)
                        .with("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    assertThat(url.contains(SIGN_UP_PAGE)).isTrue();
                });
    }

    private String getResponseBody(byte[] responseBody) {
        try {
            return new String(responseBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("ArticleControllerTest 에서 EncodingException 발생 : " + e.getMessage());
        }
    }

    @Test
    void 로그인_성공_리다이렉트_테스트() {
        webTestClient.post().uri("users/login")
                .body(fromFormData("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_아이디_실패_리다이렉트_테스트() {
        webTestClient.post().uri("users/login")
                .body(fromFormData("email", WRONG_EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    assertThat(url.contains(LOGIN_PAGE)).isTrue();
                });

    }

    @Test
    void 로그인_패스워드_실패_리다이렉트_테스트() {
        webTestClient.post().uri("users/login")
                .body(fromFormData("email", EMAIL)
                        .with("password", WRONG_PASSWORD))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    assertThat(url.contains(LOGIN_PAGE)).isTrue();
                });

    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/users")
                .exchange();
    }
}