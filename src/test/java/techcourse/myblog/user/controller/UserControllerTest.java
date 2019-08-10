package techcourse.myblog.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.data.UserDataForTest;
import techcourse.myblog.template.RequestTemplate;

public class UserControllerTest extends RequestTemplate {

    @Test
    void 가입_페이지_이동() {
        loggedOutGetRequest("/users/new")
                .expectStatus()
                .isOk();
    }

    @Test
    void 가입_성공() {
        loggedOutPostRequest("/users")
                .body(BodyInserters.fromFormData("email", UserDataForTest.NEW_USER_EMAIL)
                        .with("password", UserDataForTest.NEW_USER_PASSWORD)
                        .with("name", UserDataForTest.NEW_USER_NAME))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 가입_이메일_실패() {
        loggedOutPostRequest("/users")
                .body(BodyInserters.fromFormData("email", "wrongEmail")
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", UserDataForTest.USER_NAME))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 가입_패스워드_실패() {
        loggedOutPostRequest("/users")
                .body(BodyInserters.fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", "wrongPassword")
                        .with("name", UserDataForTest.USER_NAME))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 가입_이름_실패() {
        loggedOutPostRequest("/users")
                .body(BodyInserters.fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", "wrongNameNameName"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 유저리스트_페이지_이동() {
        loggedOutGetRequest("/users")
                .expectStatus()
                .isOk();
    }

    @Test
    void 마이페이지_이동() {
        loggedInGetRequest("/users/1")
                .expectStatus()
                .isOk();
    }

    @Test
    void 정보수정_페이지_이동() {
        loggedInGetRequest("/users/1/edit")
                .expectStatus()
                .isOk();
    }

    @Test
    void 유저정보_업데이트() {
        loggedInPutRequest("/users/1")
                .body(BodyInserters.fromFormData("name", "UpdateName"))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 유저삭제() {
        loggedInDeleteRequest("/users/2", UserDataForTest.USER_EMAIL_TWO, UserDataForTest.USER_PASSWORD)
                .expectStatus()
                .isFound();
    }
}
