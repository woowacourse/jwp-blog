package techcourse.myblog.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.user.dto.UserCreateDto;
import techcourse.myblog.util.UserUtilForTest;
import techcourse.myblog.util.WebTest;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.user.UserDataForTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    private static final long USER_ID = 1;
    private static final long USER_DELETE_ID = 2;
    private static final String UNFORMATTED_NAME_ERROR = "올바른 이름 형식이 아닙니다.";
    private static final String UNFORMATTED_PASSWORD_ERROR = "올바른 비밀번호 형식이 아닙니다.";
    private static final String UNFORMATTED_EMAIL_ERROR = "올바른 email 형식이 아닙니다.";

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    @BeforeEach
    void setUp() {
        cookie = UserUtilForTest.loginAndGetCookie(webTestClient);
    }

    @Test
    void 회원가입_페이지_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/signup", EMPTY_COOKIE)
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_테스트() {
        UserCreateDto userCreateDto = new UserCreateDto(NEW_USER_EMAIL, NEW_USER_PASSWORD, NEW_USER_NAME);

        executeSignUp(userCreateDto).isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.email").isNotEmpty()
                .jsonPath("$.email").isEqualTo(NEW_USER_EMAIL)
                .jsonPath("$.name").isNotEmpty()
                .jsonPath("$.name").isEqualTo(NEW_USER_NAME);
    }

    @Test
    void 회원가입_요청_이메일_중복_실패_테스트() {
        String duplicatedEmail = "email@gmail.com";
        String errorMessage = "[ " + duplicatedEmail + " ]은 중복된 이메일입니다.";
        UserCreateDto userCreateDto = new UserCreateDto(duplicatedEmail, NEW_USER_PASSWORD, NEW_USER_NAME);

        signUpFail(userCreateDto, errorMessage);
    }

    @Test
    void 회원가입_요청_이름_형식_실패_테스트() {
        String wrongName = "";
        UserCreateDto userCreateDto = new UserCreateDto(NEW_USER_EMAIL, NEW_USER_PASSWORD, wrongName);

        signUpFail(userCreateDto, UNFORMATTED_NAME_ERROR);
    }

    @Test
    void 회원가입_요청_패스워드_실패_테스트() {
        String wrongPassword = "password";
        UserCreateDto userCreateDto = new UserCreateDto(NEW_USER_EMAIL, wrongPassword, NEW_USER_NAME);

        signUpFail(userCreateDto, UNFORMATTED_PASSWORD_ERROR);
    }

    @Test
    void 회원가입_요청_이메일_형식_실패_테스트() {
        String wrongEmail = "email";
        UserCreateDto userCreateDto = new UserCreateDto(wrongEmail, NEW_USER_PASSWORD, NEW_USER_NAME);

        signUpFail(userCreateDto, UNFORMATTED_EMAIL_ERROR);
    }

    @Test
    void 회원_정보_전체_조회_테스트() {
        WebTest.executeGetTest(webTestClient, "/users", cookie)
                .expectStatus().isOk();
    }

    @Test
    void MyPage_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/mypage/" + USER_ID, cookie)
                .expectStatus().isOk();
    }

    @Test
    void EditMyPage_이동_성공_테스트() {
        WebTest.executeGetTest(webTestClient, "/mypage/" + USER_ID + "/edit", cookie)
                .expectStatus().isOk();
    }

    @Test
    void 회원_정보_수정_성공_테스트() {
        String newName = "newName";

        EntityExchangeResult<byte[]> entityExchangeResult =
                WebTest.executePutTest(webTestClient, "/users/" + USER_ID, cookie,
                        BodyInserters.fromFormData("name", newName))
                        .expectStatus().isFound()
                        .expectBody()
                        .returnResult();

        String location = String.valueOf(entityExchangeResult.getResponseHeaders().getLocation());
        WebTest.executeGetTest(webTestClient, location, cookie)
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(newName)).isTrue();
                });
    }

    @Test
    void 회원_정보_수정_실패_테스트() {
        String wrongName = "";

        WebTest.executePutTest(webTestClient, "/users/" + USER_ID, cookie,
                BodyInserters.fromFormData("name", wrongName))
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원_탈퇴_성공_테스트() {
        WebTest.executeDeleteTest(webTestClient, "/users/" + USER_DELETE_ID, cookie)
                .expectStatus().isFound();
    }

    private StatusAssertions executeSignUp(UserCreateDto userCreateDto) {
        return WebTest.executePostTestWithJson(webTestClient, "/users", EMPTY_COOKIE)
                .body(Mono.just(userCreateDto), UserCreateDto.class)
                .exchange()
                .expectStatus();
    }

    private void signUpFail(UserCreateDto userCreateDto, String errorMessage) {
        executeSignUp(userCreateDto).isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.errorMessage").isNotEmpty()
                .jsonPath("$.errorMessage").isEqualTo(errorMessage);
    }
}