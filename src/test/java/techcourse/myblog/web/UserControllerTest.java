package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void showSignupFormTest() {
        webTestClient.get().uri("/signup")
                            .exchange()
                            .expectStatus()
                            .isOk();
    }

    @Test
    void registerUserTest() {
        webTestClient.post().uri("/users")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .body(
                                    BodyInserters.fromFormData("name", "donut")
                                                .with("email", "donut@woowa.com")
                                                .with("password", "qwer1234")
                            ).exchange()
                            .expectStatus()
                            .is3xxRedirection();
    }

    @Test
    void showLoginFormTest() {
        webTestClient.get().uri("/login")
                            .exchange()
                            .expectStatus()
                            .isOk();
    }

    @Test
    void showUsersList() {
        webTestClient.get().uri("/users")
                            .exchange()
                            .expectStatus()
                            .isOk();
    }
}