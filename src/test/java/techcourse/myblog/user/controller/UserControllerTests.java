package techcourse.myblog.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.util.UserUtilForTest;
import techcourse.myblog.util.WebTest;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.user.UserDataForTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    private static final long USER_ID = 1;
    private static final long USER_DELETE_ID = 2;

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
        WebTest.executePostTest(webTestClient, "/users", EMPTY_COOKIE,
                NEW_USER_BODY)
                .expectStatus().isFound();
    }

    @Test
    void 회원가입_요청_이메일_중복_실패_테스트() {
        String duplicatedEmail = "email@gmail.com";

        WebTest.executePostTest(webTestClient, "/users", EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", duplicatedEmail)
                        .with("password", USER_PASSWORD)
                        .with("name", USER_NAME))
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이름_형식_실패_테스트() {
        String wrongName = "";

        WebTest.executePostTest(webTestClient, "/users", EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", USER_EMAIL)
                        .with("password", USER_PASSWORD)
                        .with("name", wrongName))
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_패스워드_실패_테스트() {
        String wrongPassword = "password";

        WebTest.executePostTest(webTestClient, "/users", EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", USER_EMAIL)
                        .with("password", wrongPassword)
                        .with("name", USER_NAME))
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이메일_형식_실패_테스트() {
        String wrongEmail = "email";

        WebTest.executePostTest(webTestClient, "/users", EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", wrongEmail)
                        .with("password", USER_PASSWORD)
                        .with("name", USER_NAME))
                .expectStatus().isBadRequest();
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
}