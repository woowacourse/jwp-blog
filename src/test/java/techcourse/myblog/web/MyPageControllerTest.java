package techcourse.myblog.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyPageControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("마이 페이지를 보여준다.")
    public void showMyPage() {
        webTestClient.get()
                .uri("/mypage/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인이 되어 있는 경우에 마이 페이지 수정화면을 보여준다.")
    public void showMyPageEditWhenLogIn() {
        webTestClient.get()
                .uri("/mypage/1/edit")
                .cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인이 되어 있지 않은 경우에 마이 페이지 수정 화면으로 접근하면 홈 화면으로 리다이렉트한다.")
    public void showMyPageEditWhenLogOut() {
        webTestClient.get()
                .uri("/mypage/1/edit")
                .exchange()
                .expectStatus().isFound();
    }
}