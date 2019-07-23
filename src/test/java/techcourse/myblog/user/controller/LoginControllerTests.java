package techcourse.myblog.user.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.user.UserDataForTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", UserDataForTest.USER_NAME))
                .exchange();
    }

    @Test
    void 로그인_페이지_이동_테스트() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_요청_성공_테스트() {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 로그인_요청_존재하지_않는_이메일_테스트() {
        String noneEmail = "none@gamil.com";

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", noneEmail)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_요청_패스워드_불일치_테스트() {
        String wrongPassword = "wrong1234!";

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", wrongPassword))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_요청_아이디_형식_오류_테스트() {
        String invalidFormEmail = "email";

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", invalidFormEmail)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_요청_비밀번호_형식_오류_테스트() {
        String invalidFormPassword = "password";

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", invalidFormPassword))
                .exchange()
                .expectStatus().isBadRequest();
    }
}