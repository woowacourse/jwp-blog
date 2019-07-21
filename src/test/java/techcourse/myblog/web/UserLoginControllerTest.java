package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLoginControllerTest {
    private static final String name = "미스터코";
    private static final String email = "test@test.com";
    private static final String password = "123123123";

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int localServerPort;

    @BeforeEach
    void setUp() {
        // 추가
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
        // 로그인
        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader()
                .valueMatches("location", "http://localhost:" + localServerPort + "/")
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
                .exchange()
                .expectHeader()
                .valueMatches("location", "http://localhost:" + localServerPort + "/")
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    public void 회원조회페이지_이동() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();
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

    @Test
    public void 회원탈퇴() {
        webTestClient.delete()
                .uri("/users/1/mypage-edit")
                .exchange()
                .expectHeader()
                .valueMatches("location", "http://localhost:" + localServerPort + "/logout")
                .expectStatus()
                .is3xxRedirection();
    }

    @AfterEach
    void tearDown() {
        // 회원 탈퇴
        webTestClient.delete()
                .uri("/users/{id}/mypage-edit")
                .exchange()
                .expectHeader()
                .valueMatches("location", "http://localhost:" + localServerPort + "/logout")
                .expectStatus()
                .is3xxRedirection();
    }
}
