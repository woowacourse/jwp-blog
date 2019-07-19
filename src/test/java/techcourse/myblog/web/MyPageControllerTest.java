package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static techcourse.myblog.service.UserServiceTest.VALID_PASSWORD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyPageControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("로그인이 되어 있는 경우에 마이 페이지를 보여준다.")
    void showMyPageWhenLogIn() {
        String name = "testName";
        String email = "mypagetest1@woowa.com";
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
                .expectStatus().isFound();

        webTestClient.get()
                .uri("/mypage")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인이 되어 있지 않은 경우에 메인 페이지로 이동한다.")
    void showMyPageWhenLogOut() {
        webTestClient.get()
                .uri("/mypage")
                .exchange()
                .expectStatus().isFound();
    }

    @AfterEach
    void tearDown() {
        webTestClient.get().uri("/logout");
    }
}