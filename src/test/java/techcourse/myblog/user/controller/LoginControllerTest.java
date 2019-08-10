package techcourse.myblog.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.data.UserDataForTest;
import techcourse.myblog.template.RequestTemplate;

public class LoginControllerTest extends RequestTemplate {
    @Test
    void 로그인_페이지_이동() {
        loggedOutGetRequest("/login")
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인_성공() {
        loggedOutPostRequest("/login")
                .body(BodyInserters.fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 로그인_실패() {
        loggedOutPostRequest("/login")
                .body(BodyInserters.fromFormData("email", "test@test.com")
                        .with("password", UserDataForTest.USER_PASSWORD))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 로그아웃() {
        loggedInGetRequest("/logout")
                .expectStatus()
                .isFound();
    }
}
