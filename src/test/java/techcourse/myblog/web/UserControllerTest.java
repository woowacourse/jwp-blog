package techcourse.myblog.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    int randomPortNumber;

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

    @Test
    @DisplayName("회원 가입 페이지에서 유저 정보를 넘겨받아 새로운 유저를 생성한다.")
    void createUser() {
        String name = "hibri";
        String email = "test@woowa.com";
        String password = "1234abc!!!!";

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    assertThat(location.toString())
                            .isEqualTo("http://localhost:" + randomPortNumber + "/users/login");
                });
    }
}