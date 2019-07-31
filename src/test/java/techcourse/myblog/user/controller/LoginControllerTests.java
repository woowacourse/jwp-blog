package techcourse.myblog.user.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.user.UserDataForTest;
import techcourse.myblog.util.UserUtilForTest;
import techcourse.myblog.util.WebTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        UserUtilForTest.signUp(webTestClient);
    }

    @Test
    void 로그인_페이지_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/login", UserDataForTest.EMPTY_COOKIE)
                .expectStatus().isOk();
    }

    @Test
    void 로그인_요청_성공_테스트() {
        WebTest.executePostTest(webTestClient, "/login", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .expectStatus().isFound();
    }

    @Test
    void 로그인_요청_존재하지_않는_이메일_테스트() {
        String noneEmail = "none@gamil.com";

        WebTest.executePostTest(webTestClient, "/login", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", noneEmail)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_요청_패스워드_불일치_테스트() {
        String wrongPassword = "wrong1234!";

        WebTest.executePostTest(webTestClient, "/login", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", wrongPassword))
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_요청_아이디_형식_오류_테스트() {
        String invalidFormEmail = "email";

        WebTest.executePostTest(webTestClient, "/login", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", invalidFormEmail)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_요청_비밀번호_형식_오류_테스트() {
        String invalidFormPassword = "password";

        WebTest.executePostTest(webTestClient, "/login", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", invalidFormPassword))
                .expectStatus().isBadRequest();
    }
}