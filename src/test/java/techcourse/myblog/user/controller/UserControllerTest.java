package techcourse.myblog.user.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.data.UserDataForTest;
import techcourse.myblog.template.RequestTemplate;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.domain.vo.Email;

public class UserControllerTest extends RequestTemplate {
    private User user;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 가입_페이지_이동() {
        loggedOutGetRequest("/signup")
                .expectStatus()
                .isOk();
    }

    @Test
    void 가입_성공() {
        loggedOutPostRequest("/users")
                .body(BodyInserters.fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", UserDataForTest.USER_NAME))
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
        signUp();
        user = userRepository.findByEmail(Email.of(UserDataForTest.USER_EMAIL)).get();
        loggedInGetRequest("/mypage/" + user.getId())
                .expectStatus()
                .isOk();
    }

    @Test
    void 정보수정_페이지_이동() {
        signUp();
        user = userRepository.findByEmail(Email.of(UserDataForTest.USER_EMAIL)).get();
        loggedInGetRequest("/mypage/" + user.getId() + "/edit")
                .expectStatus()
                .isOk();
    }

    @Test
    void 유저정보_업데이트() {
        signUp();
        user = userRepository.findByEmail(Email.of(UserDataForTest.USER_EMAIL)).get();
        loggedInPutRequest("/users/" + user.getId())
                .body(BodyInserters.fromFormData("name", "UpdateName"))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 유저삭제() {
        signUp();
        user = userRepository.findByEmail(Email.of(UserDataForTest.USER_EMAIL)).get();
        loggedInDeleteRequest("/users/" + user.getId())
                .expectStatus()
                .isFound();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
