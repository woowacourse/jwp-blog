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
import static techcourse.myblog.service.UserServiceTest.VALID_PASSWORD;
import static techcourse.myblog.service.exception.LogInException.LOGIN_FAIL_MESSAGE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogInControllerTest {
    @LocalServerPort
    int randomPortNumber;

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("로그인 페이지를 보여준다.")
    void showLoginPage() {
        webTestClient.get()
                .uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인 성공 시 메인 화면을 띄우고 우측 상단에 사용자 이름을 띄운다.")
    void successLogIn() {
        String name = "testName";
        String email = "test1@woowa.com";
        String password = VALID_PASSWORD;
        String passwordConfirm = VALID_PASSWORD;

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("passwordConfirm", passwordConfirm))
                .exchange()
                .expectStatus().isFound();

        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get()
                            .uri(location)
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body).contains(name);
                            });
                });
    }

    @Test
    @DisplayName("로그인 실패시 에러 메세지 출력한다.")
    void failLogIn() {
        String name = "testName";
        String email = "test2@woowa.com";
        String password = VALID_PASSWORD;
        String passwordConfirm = VALID_PASSWORD;
        String diffEmail = "diff@woowa.com";

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("passwordConfirm", passwordConfirm))
                .exchange()
                .expectStatus().isFound();

        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", diffEmail)
                        .with("password", password))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(LOGIN_FAIL_MESSAGE);
                });
    }
}