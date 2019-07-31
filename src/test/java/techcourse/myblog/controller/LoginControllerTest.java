package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest extends MyblogApplicationTests {
    private WebTestClient webTestClient;

    private UserRepository userRepository;

    @Autowired
    public LoginControllerTest(WebTestClient webTestClient, UserRepository userRepository) {
        this.webTestClient = webTestClient;
        this.userRepository = userRepository;
    }

    @Test
    void 로그인_테스트() {
        testSuccessLogin(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD);
    }

    @Test
    void showLoginPage() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void showLoginPage_로그인_상태일_경우() {
        String cookie = getLoginCookie(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD);

        webTestClient.get().uri("/login").header("Cookie",cookie)
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isFalse();
                })
        ;
    }

    @Test
    void 로그인_테스트_실패_아이디_틀림() {
        String wrongEmail = "aa";

        testFailLogin(DEFAULT_USER_PASSWORD, wrongEmail);
    }

    @Test
    void 로그인_테스트_실패_비밀번호_틀림() {
        String wrongPW = "aa";

        testFailLogin(wrongPW, DEFAULT_USER_EMAIL);
    }

    @Test
    void 로그아웃_성공() {
        testSuccessLogout();
    }

    @Test
    void 로그아웃_실패_로그인_안했을_경우() {
        testFailLogout();
    }

    protected String getLoginCookie(String email, String password) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    private void testSuccessLogin(String email, String password) {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isFalse();
                })
        ;
    }

    private void testFailLogin(String wrongPW, String testEmail) {
        String errorMessgae = "아이디나 비밀번호가 잘못되었습니다.";
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", testEmail)
                        .with("password", wrongPW))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(errorMessgae)).isTrue();
        });
        ;
    }

    private void testSuccessLogout() {
        String cookie = getLoginCookie(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD);

        webTestClient.get().uri("/logout").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(innerResponse -> {
                    assertThat(innerResponse.getResponseHeaders().getLocation().toString().contains("login")).isFalse();
                });
    }

    private void testFailLogout() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isTrue();
                });
    }
}