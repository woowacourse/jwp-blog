package techcourse.myblog.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.Utils.TestUtils.getBody;
import static techcourse.myblog.Utils.TestUtils.logIn;
import static techcourse.myblog.Utils.TestConstants.*;
import static techcourse.myblog.domain.exception.UserArgumentException.*;
import static techcourse.myblog.service.exception.SignUpException.SIGN_UP_FAIL_MESSAGE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    int randomPortNumber;

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("회원가입 페이지를 보여준다.")
    void showRegisterPage() {
        webTestClient.get()
                .uri("/users/sign-up")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("유저 목록을 보여준다.")
    void showUserList() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("회원 가입 페이지에서 유저 정보를 넘겨받아 새로운 유저를 생성한다.")
    void createUser() {
        requestSignUp("hibri", "test1@woowa.com", VALID_PASSWORD, VALID_PASSWORD)
                .expectStatus().isFound()
                .expectHeader()
                .valueMatches("location", "http://localhost:" + randomPortNumber + "/login");
    }

    @Test
    @DisplayName("email이 중복되는 경우 error message를 담은 페이지를 되돌려준다.")
    void isDuplicatedEmail() {
        WebTestClient.ResponseSpec responseSpec =
                requestSignUp("new name", BASE_USER_EMAIL, VALID_PASSWORD, VALID_PASSWORD)
                        .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SIGN_UP_FAIL_MESSAGE, EMAIL_DUPLICATION_MESSAGE);
    }

    @Test
    @DisplayName("이름이 2자 미만인 경우 error message를 담은 페이지를 되돌려준다.")
    void underValidNameLength() {
        WebTestClient.ResponseSpec responseSpec =
                requestSignUp("a", "test3@woowa.com", VALID_PASSWORD, VALID_PASSWORD)
                        .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SIGN_UP_FAIL_MESSAGE, INVALID_NAME_LENGTH_MESSAGE);
    }

    @Test
    @DisplayName("이름이 10자 초과인 경우 error message를 담은 페이지를 되돌려준다.")
    void exceedValidNameLength() {
        WebTestClient.ResponseSpec responseSpec =
                requestSignUp("abcdefghijk", "test4@woowa.com", VALID_PASSWORD, VALID_PASSWORD)
                        .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SIGN_UP_FAIL_MESSAGE, INVALID_NAME_LENGTH_MESSAGE);
    }

    @Test
    @DisplayName("잘못된 이름인 경우 error message를 담은 페이지를 되돌려준다.")
    void checkInvalidName() {
        WebTestClient.ResponseSpec responseSpec =
                requestSignUp("afghij1", "test5@woowa.com", VALID_PASSWORD, VALID_PASSWORD)
                        .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SIGN_UP_FAIL_MESSAGE, NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE);
    }

    @Test
    @DisplayName("비밀번호 길이가 8자 미만인 경우 error message를 담은 페이지를 되돌려준다.")
    void checkInvalidPasswordLength() {
        WebTestClient.ResponseSpec responseSpec =
                requestSignUp(VALID_NAME, "test6@woowa.com", "paSWO1!", "paSWO1!")
                        .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SIGN_UP_FAIL_MESSAGE, INVALID_PASSWORD_LENGTH_MESSAGE);
    }

    @Test
    @DisplayName("잘못된 비밀번호인 경우 error message를 담은 페이지를 되돌려준다.")
    void checkInvalidPassword() {
        WebTestClient.ResponseSpec responseSpec =
                requestSignUp(VALID_NAME, "test7@test.test", "wrong password", "wrong password")
                        .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SIGN_UP_FAIL_MESSAGE, INVALID_PASSWORD_MESSAGE);
    }

    @Test
    @DisplayName("비밀번호 확인과 비밀번호가 다른 경우 에러 메시지를 담은 페이지를 되돌려준다.")
    void confirmPassword() {
        WebTestClient.ResponseSpec responseSpec =
                requestSignUp(VALID_NAME, "test7@woowa.com", VALID_PASSWORD, VALID_PASSWORD + "diff")
                        .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(SIGN_UP_FAIL_MESSAGE, PASSWORD_CONFIRM_FAIL_MESSAGE);
    }

    @Test
    @DisplayName("로그인한 유저가 자신의 프로필을 변경하는 경우 변경에 성공한다.")
    void updateUserWhenLogIn() {
        webTestClient.put()
                .uri("/users/" + UPDATE_USER_ID)
                .cookie("JSESSIONID", logIn(webTestClient, UPDATE_USER_EMAIL, UPDATE_USER_PASSWORD))
                .body(BodyInserters.fromFormData("name", "updated")
                        .with("email", UPDATE_USER_EMAIL))
                .exchange()
                .expectStatus().isFound();

        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/mypage/" + UPDATE_USER_ID)
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains("updated");
    }

    @Test
    @DisplayName("로그인 되어있지 않을 때 프로필을 변경하는 경우 변경되지 않는다.")
    void updateUserWhenLogOut() {
        webTestClient.put()
                .uri("/users/" + BASE_USER_ID)
                .body(BodyInserters.fromFormData("name", "updated")
                        .with("email", BASE_USER_EMAIL))
                .exchange()
                .expectStatus().isFound();

        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/mypage/" + BASE_USER_ID)
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(BASE_USER_NAME);
    }

    @Test
    @DisplayName("로그인한 유저가 자신의 프로필을 삭제하는 경우 삭제에 성공한다.")
    void deleteUserWhenLogIn() {
        webTestClient.delete()
                .uri("/users/" + DELETE_USER_ID)
                .cookie("JSESSIONID", logIn(webTestClient, DELETE_USER_EMAIL, DELETE_USER_PASSWORD))
                .exchange()
                .expectStatus().isFound();

        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).doesNotContain(DELETE_USER_EMAIL);
    }

    @Test
    @DisplayName("로그인 되어있지 않을 때 회원 탈퇴하려는 경우 실패한다.")
    void deleteUserWhenLogOut() {
        webTestClient.delete()
                .uri("/users/" + BASE_USER_ID)
                .exchange()
                .expectStatus().isFound();

        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();

        assertThat(getBody(responseSpec)).contains(BASE_USER_EMAIL);
    }

    private WebTestClient.ResponseSpec requestSignUp(String name, String email, String password, String passwordConfirm) {
        return webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password)
                        .with("passwordConfirm", passwordConfirm))
                .exchange();
    }
}