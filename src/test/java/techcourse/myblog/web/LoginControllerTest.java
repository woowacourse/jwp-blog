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
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Buddy")
                        .with("email", "buddy@buddy.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange();

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "buddy@buddy.com")
                        .with("password", "exception-password"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void 로그인_패스워드_불일치() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "ssosso")
                        .with("email", "ssosso@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange();

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "ssosso@gmail.com")
                        .with("password", "exception-password"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}
