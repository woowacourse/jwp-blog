package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTests {
    private static final String DEFAULT_USER_EMAIL = "john123@example.com";
    private static final String DEFAULT_USER_PASSWORD = "p@ssW0rd";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 로그인_화면_이동_확인() {
        webTestClient.get().uri("/login")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 로그인_성공() {
        requestLogin(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD)
            .expectStatus().is3xxRedirection()
            .expectHeader().valueMatches("Location", ".*localhost:[0-9]+/.*");
    }

    private WebTestClient.ResponseSpec requestLogin(String email, String password) {
        return webTestClient.post().uri("/users/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("email", email)
                .with("password", password))
            .exchange();
    }

    @Test
    void 이메일이_없는경우() {
        requestLogin("done@gmail.com", DEFAULT_USER_PASSWORD)
            .expectStatus().isNoContent();
    }

    @Test
    void 비밀번호_불일치() {
        requestLogin(DEFAULT_USER_EMAIL, "wrongPassword")
            .expectStatus().is4xxClientError();
    }
}
