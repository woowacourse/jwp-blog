package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @Test
    void showLoginPage() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void login() {
        userRepository.save(new User("Martin", "oeeen3@gmail.com", "Aa12345!"));
        webTestClient.post().uri("/login/check")
                .body(BodyInserters
                        .fromFormData("email", "oeeen3@gmail.com")
                        .with("password", "Aa12345!"))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void logout() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound();
    }
}
