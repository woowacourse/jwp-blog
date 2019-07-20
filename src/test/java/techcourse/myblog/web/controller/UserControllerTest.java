package techcourse.myblog.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.function.Consumer;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {

    }

    @Test
    void signUp_test() {
        signUpExchange(entityExchangeResult -> {});
    }

    private WebTestClient.BodyContentSpec signUpExchange(Consumer<EntityExchangeResult<byte[]>> consumer) {
        return webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", "aiden")
                        .with("email", "aiden@naver.com")
                        .with("password", "aidenAIDEN1!"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/login.*")
                .expectBody().consumeWith(consumer);
    }

    @Test
    void login_test_true() {
        signUpExchange(entityExchangeResult -> {
            webTestClient.post().uri("/login")
                    .body(BodyInserters.fromFormData("email", "aiden@naver.com")
                            .with("password", "aidenAIDEN1!")
                    ).exchange()
                    .expectStatus().is3xxRedirection()
                    .expectHeader().valueMatches("Location", ".*localhost:[0-9]+/;.*");
        });
    }

    @Test
    void login_test_false() {
        signUpExchange(entityExchangeResult -> {
            webTestClient.post().uri("/login")
                    .body(BodyInserters.fromFormData("email", "aiden@naver.com")
                            .with("password", "aidenAIDEN1")
                    ).exchange()
                    .expectStatus().isOk();
        });
    }

    @Test
    void update_test() {
        String name = "whale";
        signUpExchange(entityExchangeResult -> {
            webTestClient.post().uri("/login")
                    .body(BodyInserters.fromFormData("email", "aiden@naver.com")
                            .with("password", "aidenAIDEN1!")
                    ).exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody()
                    .consumeWith(entityExchangeResult1 -> {
                        webTestClient.put().uri("/mypage/edit")
                                .body(BodyInserters.fromFormData("name", name))
                                .exchange()
                                .expectStatus().is3xxRedirection()
                                .expectHeader().valueMatches("Location", ".*/mypage.*");
                    });
        });
    }
}
