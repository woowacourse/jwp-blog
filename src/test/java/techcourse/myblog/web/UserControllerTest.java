package techcourse.myblog.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("회원 가입 요청시 성공하는 테스트")
    void signUpTest() {
        webTestClient.post()
                .uri("/users")
                .body(BodyInserters.fromFormData("name", "test")
                        .with("email", "test@test.com")
                        .with("password", "PassW0rd@")
                        .with("passwordConfirm", "PassW0rd@"))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    @DisplayName("회원 가입 요청시 잘못된 패스워드 패턴으로 인해 실패하는 테스트")
    void notSignUpTest() {
        webTestClient.post()
                .uri("/users")
                .body(BodyInserters.fromFormData("name", "test")
                        .with("email", "test@test.com")
                        .with("password", "PassWord")
                        .with("passwordConfirm", "PassWord"))
                .exchange()
                .expectStatus().isOk();
    }
}