package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String name = "미스터코";
    private static final String email = "test@test.com";
    private static final String password = "123123123";

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int localServerPort;

    @Test
    public void 회원가입페이지_이동() {
        webTestClient.get()
                .uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원추가() {
        webTestClient.post()
                .uri("/signup")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader()
                .valueMatches("location", "http://localhost:" + localServerPort + "/login")
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    public void 회원로그인() {
        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange().expectStatus().isOk();
    }

    @Test
    public void 회원조회페이지_이동() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectHeader()
                .valueMatches("location", "http://localhost:" + localServerPort + "/login")
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    public void 유저페이지() {
        webTestClient.get()
                .uri("/users/1/mypage")
                .exchange()
                .expectHeader()
                .valueMatches("location", "http://localhost:" + localServerPort + "/login")
                .expectStatus()
                .is3xxRedirection();
    }
}