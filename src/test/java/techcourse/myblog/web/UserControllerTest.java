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
                .body(BodyInserters.fromFormData("userName","Martin")
                        .with("email","martin@gmail.com")
                        .with("password","123456")
                ).exchange()
        .expectStatus().isFound();
    }

    @Test
    void show_login(){
        webTestClient.get().uri("/users/login").exchange().expectStatus().isOk();
    }

    @Test
    void show_all_users(){
        create_user();
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
}
