package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

class UserControllerTest extends ControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        jSessionId = login(webTestClient);
    }

    @Test
    void 로그인_폼_테스트() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원_가입_테스트() {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", "envy")
                        .with("email", "envy@test.com")
                        .with("password", "12Woowa@@"))
                .exchange()
                .expectHeader().valueMatches("Location", ".*/login");
    }

    @Test
    void 로그인_성공_테스트() {
        loginResult(webTestClient, USER_EMAIL, USER_PASSWORD)
                .getStatus().is3xxRedirection();
    }

    @Test
    void 로그인_실패_테스트() {
        loginResult(webTestClient, "test@woowa.com", "12Woowa@@")
                .getStatus().is2xxSuccessful();
    }

    @Test
    void 회원_수정_테스트() {
        webTestClient.put().uri("/mypage/edit")
                .cookie(JSESSIONID, jSessionId)
                .body(BodyInserters.fromFormData("name", "test"))
                .exchange()
                .expectHeader().valueMatches("Location", ".*/mypage");
    }

    @Test
    void 회원_삭제_테스트() {
        webTestClient.delete().uri("/mypage/edit")
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectHeader().valueMatches("Location", ".*/logout");
    }
}