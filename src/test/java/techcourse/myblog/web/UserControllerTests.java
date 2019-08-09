package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static techcourse.myblog.web.ControllerTestUtil.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        signUp(webTestClient, NAME, EMAIL, PASSWORD);
    }

    @Test
    void loginForm() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void signUpForm() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 유저_동일한_email() {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", "Alice")
                        .with("email", EMAIL)
                        .with("password", "PassWord1!")
                        .with("reconfirmPassword", "PassWord1!"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertTrue(body.contains("중복된 이메일입니다!"));
                })
        ;
    }

    @Test
    void 로그인안하고_myPage_페이지접근_로그인화면으로_이동() {
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/login")
                .expectStatus()
                .is3xxRedirection()
        ;
    }

    @Test
    void 로그인안하고_myPage_Edit_페이지접근_로그인화면으로_이동() {
        webTestClient.get().uri("/mypage-edit")
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/login")
                .expectStatus()
                .is3xxRedirection()
        ; // 로그인 화면으로 갈 것임
    }

    @Test
    void 로그인_후_userlist_정상_이동() {
        String cookie = login(webTestClient, EMAIL, PASSWORD);
        webTestClient.get().uri("/users")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 로그인_후_mypage_정상_이동() {
        String cookie = login(webTestClient, EMAIL, PASSWORD);
        webTestClient.get().uri("/mypage")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 로그인_후_mypageedit_정상_이동() {
        String cookie = login(webTestClient, EMAIL, PASSWORD);
        webTestClient.get().uri("/mypage-edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 회원_정상_탈퇴() {
        webTestClient.delete().uri("/users/1")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
        ;
    }
}
