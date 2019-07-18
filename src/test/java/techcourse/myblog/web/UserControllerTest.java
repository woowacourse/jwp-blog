package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void show_sign_up() {
        webTestClient.get().uri("/users/signup").exchange().expectStatus().isOk();
    }

    @Test
    void create_user() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Brown")
                        .with("email", "brown@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus().isFound();
    }

    @Test
    void show_login() {
        webTestClient.get().uri("/login").exchange().expectStatus().isOk();
    }

    @Test
    void show_all_users() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("Martin")).isTrue();
            assertThat(body.contains("martin@gmail.com")).isTrue();
        });
    }

    @Test
    void check_same_email() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "buddy@buddy.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("중복된 이메일 입니다.")).isTrue();
        });
    }

    @Test
    void check_valid_name() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "M")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword", "Aa12345!")
                ).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("형식에 맞는 이름이 아닙니다.")).isTrue();
        });
    }

    @Test
    void check_valid_password() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "A")
                        .with("confirmPassword", "A")
                ).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("형식에 맞는 비밀번호가 아닙니다.")).isTrue();
        });
    }

    @Test
    void check_valid_confirm_password() {
        webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", "Martin")
                        .with("email", "martin@gmail.com")
                        .with("password", "Aa12345!")
                        .with("confirmPassword","Ss12345!")
                ).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("비밀번호가 일치하지 않습니다.")).isTrue();
        });
    }

    @Test
    void showUserEdit() {
        webTestClient.get().uri("/users/mypage/edit")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
