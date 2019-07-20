package techcourse.myblog.web;

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
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private String VALID_NAME = "유효한이름";
    private String VALID_EMAIL = "valid@email.com";
    private String VALID_PASSWORD = "ValidPassword!123";

    private String INVALID_NAME = "1nva1id";
    private String INVALID_EMAIL = "invalidemail";
    private String INVALID_PASSWORD = "invalidpw";

    @Test
    public void 회원가입_페이지_테스트() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원가입_테스트() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", VALID_NAME)
                        .with("email", VALID_EMAIL)
                        .with("password", VALID_PASSWORD))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri(response.getResponseHeaders().getLocation())
                            .exchange()
                            .expectStatus()
                            .isOk();
                });
    }

    @Test
    public void 유효햐지_않은_이름으로_회원가입_하는_경우_예외처리() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", INVALID_NAME)
                        .with("email", VALID_EMAIL)
                        .with("password", VALID_PASSWORD))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void 유효하지_않은_이메일로_회원가입_하는_경우_예외처리() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", VALID_NAME)
                        .with("email", INVALID_EMAIL)
                        .with("password", VALID_PASSWORD))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void 유효하지_않은_비밀번호로_회원가입_하는_경우_예외처리() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", VALID_NAME)
                        .with("email", VALID_EMAIL)
                        .with("password", INVALID_PASSWORD))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void 회원조회_페이지_테스트() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "코니코니")
                        .with("email", "이메일이메일")
                        .with("password", "비번비번"))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri("/users")
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(
                                    res -> {
                                        String body = new String(res.getResponseBody());
                                        assertThat(body.contains("코니코니")).isTrue();
                                        assertThat(body.contains("이메일이메일")).isTrue();
                                    }
                            );
                });
    }
}