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
    private static final String USER_NAME_1 = "test1";
    private static final String USER_NAME_2 = "test2";
    private static final String EMAIL_1 = "test1@test.com";
    private static final String PASSWORD_1 = "1234";
    private static final String PASSWORD_2 = "12345";


    private String cookie;

    @Autowired
    WebTestClient webTestClient;

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
    void 회원가입_폼_테스트() {
        webTestClient.get().uri("/users/signup")
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void 유저_조회_테스트() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = getResponseBody(response.getResponseBody());
                    assertThat(body.contains(USER_NAME_1)).isTrue();
                    assertThat(body.contains(EMAIL_1)).isTrue();
                });
    }

    @Test
    void 유저_이메일_중복_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_1)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_1))
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
    void 수정_테스트() {
        webTestClient.post().uri("/users/mypage")
                .body(fromFormData("userName", USER_NAME_2)
                        .with("email", EMAIL_1)
                        .with("password", PASSWORD_2))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String url = response.getResponseHeaders().get("Location").get(0);
                    webTestClient.get().uri(url)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = getResponseBody(redirectResponse.getResponseBody());
                                assertThat(body.contains(USER_NAME_2)).isTrue();
                            });
                });
    }

    @Test
    void 삭제_테스트() {
        webTestClient.delete().uri("/users").header("Cookie", cookie)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/users").header("Cookie", cookie)
                .exchange();
    }
}