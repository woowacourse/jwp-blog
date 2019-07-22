package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void 로그인_페이지_접근() {
        webTestClient.get().uri("/login").exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그아웃_페이지_접근() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 로그인_없는_이메일() {
        getResponseSpec("CU@gmail.com", "Aa12345!")
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 로그인_패스워드_불일치() {
        create_user("ssosso", "ssosso@gmail.com", "Aa12345!");

        getResponseSpec("ssosso@gmail", "exception-password")
                .expectStatus()
                .isBadRequest();
    }

    private WebTestClient.ResponseSpec getResponseSpec(String email, String password) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange();
    }

    private void create_user(String userName, String email, String password) {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userName)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", password)
                ).exchange();
    }


}
