package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserSaveParams;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class LoginControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    private String jSessionId;

    @BeforeEach
    void setUp() {
        LoginTestConfig.signUp(webTestClient);
        jSessionId = LoginTestConfig.getJSessionId(webTestClient);
    }

    @Test
    void login_페이지_이동() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void signUp_페이지_이동() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void login() {
        UserSaveParams userSaveParams = LoginTestConfig.getUserSaveParams();

        webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", userSaveParams.getEmail())
                        .with("password", userSaveParams.getPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void logout() {
        webTestClient.get().uri("/logout")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @AfterEach
    void tearDown() {
        LoginTestConfig.deleteUser(webTestClient);
    }
}