package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email")
                        .with("password", "password")
                        .with("name", "name"))
                .exchange();
    }

    @Test
    void 회원가입_페이지_이동_테스트() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_요청_성공_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "newEmail")
                        .with("password", "newPassword")
                        .with("name", "newName"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("errorMessage")).isFalse();
                });
    }

    @Test
    void 회원가입_요청_실패_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email")
                        .with("password", "password")
                        .with("name", "name"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("errorMessage")).isTrue();
                });
    }
}
