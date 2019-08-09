package techcourse.myblog.web.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static techcourse.myblog.web.ControllerTestUtil.*;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthInterceptorTest {

    private String cookie;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        // 회원 가입
        signUp(webTestClient, NAME, EMAIL, PASSWORD);

        // 로그인

        cookie = login(webTestClient, EMAIL, PASSWORD);
    }

    @Test
    void 로그인후_userlist_페이지_접근() {
        login(webTestClient, EMAIL, PASSWORD);
        webTestClient.get().uri("/users")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인하지_않고_userlist_페이지_접근_Redirect() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("Location", ".*/login.*")
        ;
    }

    @Test
    void 로그인후_mypage_페이지_접근() {
        webTestClient.get().uri("/mypage")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인하지_않고_mypage_페이지_접근_Redirect() {
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/login")
        ;
    }

    @Test
    void 로그인후_mypage_edit_페이지_접근() {
        webTestClient.get().uri("/mypage-edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인하지_않고_mypage_edit_페이지_접근_Redirect() {
        webTestClient.get().uri("/mypage-edit")
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/login")
        ;
    }

    @Test
    void 로그인하지_않고_댓글_작성_Redirect() {
        webTestClient.post().uri("/articles/1/comments")
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/login");
    }

}
