package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void 회원가입페이지_이동() {
        webTestClient.get()
                .uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원추가() {
        String name = "미스터코";
        String email = "test@test.com";
        String password = "123123123";

        webTestClient.post()
                .uri("/users")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    public void 회원로그인() {
        회원추가();
        String email = "test@test.com";
        String password = "123123";

        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().isFound();
        //TODO : 세션 정보 확인하기
    }

    @Test
    public void 회원로그아웃() {
        webTestClient.get()
                .uri("/logout")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    public void 회원조회페이지_이동() {
        회원추가();

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 유저페이지() {
        회원추가();

        webTestClient.get()
                .uri("/users/1/mypage")
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/users/2/mypage")
                .exchange()
                .expectStatus().isFound();
    }
}