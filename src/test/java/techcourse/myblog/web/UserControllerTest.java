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
}