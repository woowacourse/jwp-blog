package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;

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
    void 회원가입_성공() {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", "이름")
                        .with("email", "test@test.com")
                        .with("password", "password"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원목록_페이지_이동() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void login() {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", "이름")
                        .with("email", "test@test.com")
                        .with("password", "password"))
                .exchange();

        webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("email", "test@test.com")
                        .with("password", "password"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void logout() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void mypage_페이지_이동() {
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void mypage_confirm_페이지_이동() {
        webTestClient.get().uri("/mypage/edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void mypage_edit_페이지_이동() {
        webTestClient.post().uri("/mypage/edit")
                .body(BodyInserters
                        .fromFormData("password", "pwd"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void mypage_delete() {
        webTestClient.delete().uri("/mypage")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}