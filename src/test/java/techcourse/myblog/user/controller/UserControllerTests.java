package techcourse.myblog.user.controller;

import org.junit.jupiter.api.AfterEach;
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
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_요청_이메일_중복_실패_테스트() {
        String duplicatedEmail = "email@gmail.com";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", duplicatedEmail)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", UserDataForTest.USER_NAME))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이름_형식_실패_테스트() {
        String wrongName = "";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", wrongName))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_패스워드_실패_테스트() {
        String wrongPassword = "password";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", wrongPassword)
                        .with("name", UserDataForTest.USER_NAME))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이메일_형식_실패_테스트() {
        String wrongEmail = "email";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", wrongEmail)
                        .with("password", UserDataForTest.USER_PASSWORD)
                        .with("name", UserDataForTest.USER_NAME))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원_정보_전체_조회_테스트() {
        webTestClient.get().uri("/users")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void MyPage_이동_테스트() {
        webTestClient.get().uri("/mypage/" + userId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void EditMyPage_이동_성공_테스트() {
        webTestClient.get().uri("/mypage/" + userId + "/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원_정보_수정_성공_테스트() {
        String newName = "newName";

        webTestClient.put().uri("/users/" + userId)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", newName))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 회원_정보_수정_실패_테스트() {
        String wrongName = "";

        webTestClient.put().uri("/users/" + userId)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", wrongName))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @AfterEach
    void 회원_탈퇴_성공_테스트() {
        webTestClient.delete().uri("/users/" + userId++)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isFound();
    }
}