package techcourse.myblog.user.controller;

import org.junit.jupiter.api.AfterEach;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    private static long userId = 1;

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    @BeforeEach
    void setUp() {
        UserUtilForTest.signUp(webTestClient);
        cookie = UserUtilForTest.loginAndGetCookie(webTestClient);
    }

    @Test
    void 회원가입_페이지_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/signup", UserDataForTest.EMPTY_COOKIE)
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_요청_이메일_중복_실패_테스트() {
        String duplicatedEmail = "email@gmail.com";

        WebTest.executePostTest(webTestClient, "/users", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", duplicatedEmail)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", UserDataForTest.USER_NAME))
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이름_형식_실패_테스트() {
        String wrongName = "";

        WebTest.executePostTest(webTestClient, "/users", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", wrongName))
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_패스워드_실패_테스트() {
        String wrongPassword = "password";

        WebTest.executePostTest(webTestClient, "/users", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", wrongPassword)
                        .with("name", UserDataForTest.USER_NAME))
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이메일_형식_실패_테스트() {
        String wrongEmail = "email";

        WebTest.executePostTest(webTestClient, "/users", UserDataForTest.EMPTY_COOKIE,
                BodyInserters
                        .fromFormData("email", wrongEmail)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", UserDataForTest.USER_NAME))
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원_정보_전체_조회_테스트() {
        WebTest.executeGetTest(webTestClient, "/users", cookie)
                .expectStatus().isOk();
    }

    @Test
    void MyPage_이동_테스트() {
        WebTest.executeGetTest(webTestClient, "/mypage/" + userId, cookie)
                .expectStatus().isOk();
    }

    @Test
    void EditMyPage_이동_성공_테스트() {
        WebTest.executeGetTest(webTestClient, "/mypage/" + userId + "/edit", cookie)
                .expectStatus().isOk();
    }

    @Test
    void 회원_정보_수정_성공_테스트() {
        String newName = "newName";

        WebTest.executePutTest(webTestClient, "/users/" + userId, cookie,
                BodyInserters.fromFormData("name", newName))
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    String location = String.valueOf(response.getResponseHeaders().getLocation());
                    WebTest.executeGetTest(webTestClient, location, cookie)
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(newName)).isTrue();
                            });
                });
    }

    @Test
    void 회원_정보_수정_실패_테스트() {
        String wrongName = "";

        WebTest.executePutTest(webTestClient, "/users/" + userId, cookie,
                BodyInserters.fromFormData("name", wrongName))
                .expectStatus().isBadRequest();
    }

    @AfterEach
    void 회원_탈퇴_성공_테스트() {
        WebTest.executeDeleteTest(webTestClient, "/users/" + userId++, cookie)
                .expectStatus().isFound();
    }
}