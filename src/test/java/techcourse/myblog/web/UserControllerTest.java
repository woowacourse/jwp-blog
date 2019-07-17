package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void register() {
        UserRequestDto userRequestDto = UserRequestDto.of("test name", "test@test.com", "testPassword12!", "testPassword12!");
        postUser(userRequestDto, response -> {
        });
    }

    private void postUser(UserRequestDto user, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
            .uri("/users")
            .body(BodyInserters.fromFormData("name", user.getName())
                .with("email", user.getEmail())
                .with("password", user.getPassword())
                .with("passwordConfirm", user.getPasswordConfirm()))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectBody()
            .consumeWith(consumer);
    }

    @Test
    void duplicate_email_alert() {
        postUser(UserRequestDto.of("john", "abcde@example.com", "p@ssW0rd", "p@ssW0rd"),
            postResponse -> {
                webTestClient.post()
                    .uri("/users")
                    .body(BodyInserters.fromFormData("name", "james")
                        .with("email", "abcde@example.com")
                        .with("password", "p@ssW0rd123")
                        .with("passwordConfirm", "p@ssW0rd123"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .consumeWith(postResponse2 -> {
                        String body = new String(postResponse2.getResponseBody());
                        assertThat(body).contains("이미 등록된 이메일입니다.");
                    });
            });
    }

    @Test
    void user_list_view() {
        UserRequestDto userRequestDto = UserRequestDto.of("john", "test_user_list_view@example.com", "p@ssW0rd", "p@ssW0rd");
        postUser(userRequestDto, postResponse -> {
            webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(postResponse2 -> {
                assertThat(new String(postResponse2.getResponseBody()))
                    .contains(userRequestDto.getName())
                    .contains(userRequestDto.getEmail());
            });
        });
    }

    @Test
    void login_logout() {
        UserRequestDto userRequestDto = UserRequestDto.of("john", "login_test@example.com", "p@ssW0rd", "p@ssW0rd");
        postUser(userRequestDto, postResponse -> {
            webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", "login_test@example.com")
                    .with("password", "p@ssW0rd"))
                .exchange().expectStatus().is3xxRedirection()
                .expectBody().consumeWith(loginResponse -> {
                String uri = loginResponse.getResponseHeaders().get("Location").get(0);
                webTestClient.get().uri(uri)
                    .exchange().expectStatus().isOk()
                    .expectBody().consumeWith(indexResponse -> {
                    assertThat(new String(indexResponse.getResponseBody())).contains("john");
                    webTestClient.get().uri("/logout")
                        .exchange().expectStatus().is3xxRedirection()
                        .expectBody().consumeWith(logoutResponse -> {
                        String logoutUri = logoutResponse.getResponseHeaders().get("Location").get(0);
                        webTestClient.get().uri(logoutUri)
                            .exchange().expectStatus().isOk()
                            .expectBody().consumeWith(indexResponse2 -> {
                            assertThat(new String(indexResponse2.getResponseBody())).doesNotContain("john");
                        });
                    });
                });
            });
        });
    }

    @Test
    void user_edit() {
        UserRequestDto userRequestDto = UserRequestDto.of("john", "edit_test@example.com", "p@ssW0rd", "p@ssW0rd");
        postUser(userRequestDto, postResponse -> {
            webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", userRequestDto.getEmail())
                    .with("password", userRequestDto.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(loginResponse -> {
                    webTestClient.get().uri(loginResponse.getResponseHeaders().get("Location").get(0))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .consumeWith(loginRedirectResponse -> {
                            webTestClient.put().uri("/users")
                                .body(BodyInserters.fromFormData("name", "kim"))
                                .exchange()
                                .expectStatus().is3xxRedirection()
                                .expectBody()
                                .consumeWith(editResponse -> {
                                    webTestClient.get().uri(editResponse.getResponseHeaders().get("Location").get(0))
                                        .exchange()
                                        .expectStatus().isOk()
                                        .expectBody()
                                        .consumeWith(editRedirectResponse -> {
                                            webTestClient.get().uri("/mypage")
                                                .exchange()
                                                .expectStatus().isOk()
                                                .expectBody()
                                                .consumeWith(indexResponse -> {
                                                    assertThat(new String(indexResponse.getResponseBody()))
                                                        .contains("kim")
                                                        .contains(userRequestDto.getEmail());
                                                });
                                        });
                                });
                        });
                });
        });
    }
}