package techcourse.myblog.controller.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static techcourse.myblog.Utils.TestUtils.logInAsBaseUser;
import static techcourse.myblog.Utils.TestConstants.BASE_USER_ID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyPageControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("마이 페이지를 보여준다.")
    void showMyPage() {
        webTestClient.get()
                .uri("/mypage/" + BASE_USER_ID)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인이 되어 있는 경우에 마이 페이지 수정화면을 보여준다.")
    void showMyPageEditWhenLogIn() {
        webTestClient.get()
                .uri("/mypage/" + BASE_USER_ID + "/edit")
                .cookie("JSESSIONID", logInAsBaseUser(webTestClient))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인이 되어 있지 않은 경우에 마이 페이지 수정 화면으로 접근하면 홈 화면으로 리다이렉트한다.")
    void showMyPageEditWhenLogOut() {
        webTestClient.get()
                .uri("/mypage/" + BASE_USER_ID + "/edit")
                .exchange()
                .expectStatus().isFound();
    }
}