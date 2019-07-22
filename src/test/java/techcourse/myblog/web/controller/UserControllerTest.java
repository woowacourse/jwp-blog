package techcourse.myblog.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private int flagNo = 1;
    private final String testEmail = "test@gmail.com";
    private final String testPassword = "aidenAIDEN1!";

    @BeforeEach
    void setUp() {
        registerUser(flagNo + testEmail);
    }

    private WebTestClient.ResponseSpec registerUser(String email) {
        return webTestClient.post().uri("/user")
                .body(fromFormData("name", "aiden")
                        .with("email", email)
                        .with("password", testPassword))
                .exchange();
    }

    @Test
    void signUp() {
        testGetMethod("/signup");
    }

    private void testGetMethod(String uri) {
        webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void loginForm() {
        testGetMethod("/login");
    }

    @Test
    void login_test_true() {
        webTestClient.post().uri("/login")
                .body(fromFormData("email", flagNo + testEmail)
                        .with("password", testPassword)
                ).exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*localhost:[0-9]+/.*");
    }

    @Test
    void login_False() {
        webTestClient.post().uri("/login")
                .body(fromFormData("email", flagNo + testEmail)
                        .with("password", "wrongPassword")
                ).exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body.contains("이메일과 비밀번호를 다시 확인해주세요.")).isTrue();
                });
    }

    @Test
    void myPage() {
        testLoggedInGetMethod("/mypage", getResponseCookie());
    }

    private void testLoggedInGetMethod(String uri, ResponseCookie responseCookie) {
        webTestClient.get().uri(uri)
                .cookie("JSESSIONID", responseCookie.getValue())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void myPageForm() {
        testLoggedInGetMethod("/mypage/edit", getResponseCookie());
    }

    @Test
    void userInfoUpdate() {
        String name = "whale";
        ResponseCookie responseCookie = getResponseCookie();

        webTestClient.put().uri("/mypage/edit")
                .cookie("JSESSIONID", Objects.requireNonNull(responseCookie).getValue())
                .body(fromFormData("name", name))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/mypage.*");
    }

    private ResponseCookie getResponseCookie() {
        return webTestClient.post().uri("/login")
                .body(fromFormData("email", flagNo + testEmail)
                        .with("password", testPassword))
                .exchange()
                .expectStatus().is3xxRedirection()
                .returnResult(ResponseCookie.class).getResponseCookies().getFirst("JSESSIONID");
    }

    @Test
    void signUp_success() {
        registerUser("signUp@woowa.com").expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/login.*");
    }

    @Test
    void signUp_duplicatedEmail_fail() {
        registerUser(flagNo + testEmail).expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body.contains("중복된 이메일입니다.")).isTrue();
                });
    }

    @Test
    void deleteUser() {
        ResponseCookie responseCookie = getResponseCookie();
        webTestClient.delete().uri("/mypage/edit")
                .cookie("JSESSIONID", responseCookie.getValue())
                .exchange();

        login_False();
    }

    @Test
    void userList() {
        testLoggedInGetMethod("/users", getResponseCookie());
    }
}
