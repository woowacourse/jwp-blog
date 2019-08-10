package techcourse.myblog.controller.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.Utils.TestConstants.BASE_USER_NAME;
import static techcourse.myblog.Utils.TestConstants.VALID_PASSWORD;
import static techcourse.myblog.Utils.TestUtils.getBody;
import static techcourse.myblog.Utils.TestUtils.logInAsBaseUser;
import static techcourse.myblog.service.exception.LogInException.NOT_FOUND_USER_MESSAGE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogInControllerTest {
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
    @DisplayName("로그아웃시 메인 화면을 띄운다.")
    void logOut() {
        webTestClient.get()
                .uri("/logout")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    @DisplayName("로그인 성공 시 메인 화면을 띄우고 우측 상단에 사용자 이름을 띄운다.")
    void successLogIn() {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(BASE_USER_NAME);
    }

    @Test
    @DisplayName("없는 이메일로 로그인 했을때 에러 메세지 출력한다.")
    void failLogIn() {
        String diffEmail = "diff@woowa.com";

        WebTestClient.ResponseSpec responseSpec = webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", diffEmail)
                        .with("password", VALID_PASSWORD))
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(NOT_FOUND_USER_MESSAGE);
    }

    @AfterEach
    void tearDown() {
        webTestClient.get().uri("/logout");
    }
}