package techcourse.myblog.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("로그인 페이지를 보여준다.")
    void showLoginPage() {
        webTestClient.get()
                .uri("/users/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("회원가입 페이지를 보여준다.")
    void showRegisterPage() {
        webTestClient.get()
                .uri("/users/sign-up")
                .exchange()
                .expectStatus().isOk();

    }
}