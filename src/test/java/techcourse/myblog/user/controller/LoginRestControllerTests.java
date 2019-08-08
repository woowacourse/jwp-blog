package techcourse.myblog.user.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.user.dto.UserLoginDto;
import techcourse.myblog.util.WebTest;

import static techcourse.myblog.user.UserDataForTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginRestControllerTests {
    private static final String UNFORMATTED_EMAIL_ERROR = "올바른 email 형식이 아닙니다.";
    private static final String UNFORMATTED_PASSWORD_ERROR = "올바른 비밀번호 형식이 아닙니다.";
    private static final String UNMATCHED_PASSWORD_ERROR = "비밀번호가 일치하지 않습니다.";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 로그인_페이지_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/login", EMPTY_COOKIE)
                .expectStatus().isOk();
    }

    @Test
    void 로그인_요청_성공_테스트() {
        UserLoginDto userLoginDto = new UserLoginDto(USER_EMAIL, USER_PASSWORD);

        executeLogin(userLoginDto).isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.email").isNotEmpty()
                .jsonPath("$.email").isEqualTo(USER_EMAIL)
                .jsonPath("$.name").isNotEmpty();
    }

    @Test
    void 로그인_요청_존재하지_않는_이메일_테스트() {
        String noneEmail = "none@gamil.com";
        String errorMessage = "[ " + noneEmail + " ]에 해당하는 계정이 존재하지 않습니다.";
        UserLoginDto userLoginDto = new UserLoginDto(noneEmail, USER_PASSWORD);

        loginFail(userLoginDto, errorMessage);
    }

    @Test
    void 로그인_요청_패스워드_불일치_테스트() {
        String wrongPassword = "wrong1234!";
        UserLoginDto userLoginDto = new UserLoginDto(USER_EMAIL, wrongPassword);

        loginFail(userLoginDto, UNMATCHED_PASSWORD_ERROR);
    }

    @Test
    void 로그인_요청_아이디_형식_오류_테스트() {
        String invalidFormEmail = "email";
        UserLoginDto userLoginDto = new UserLoginDto(invalidFormEmail, USER_PASSWORD);

        loginFail(userLoginDto, UNFORMATTED_EMAIL_ERROR);
    }

    @Test
    void 로그인_요청_비밀번호_형식_오류_테스트() {
        String invalidFormPassword = "password";
        UserLoginDto userLoginDto = new UserLoginDto(USER_EMAIL, invalidFormPassword);

        loginFail(userLoginDto, UNFORMATTED_PASSWORD_ERROR);
    }

    private StatusAssertions executeLogin(UserLoginDto userLoginDto) {
        return WebTest.executePostTestWithJson(webTestClient, "/login", EMPTY_COOKIE)
                .body(Mono.just(userLoginDto), UserLoginDto.class)
                .exchange()
                .expectStatus();
    }

    private void loginFail(UserLoginDto userLoginDto, String errorMessage) {
        executeLogin(userLoginDto).isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.errorMessage").isNotEmpty()
                .jsonPath("$.errorMessage").isEqualTo(errorMessage);
    }
}