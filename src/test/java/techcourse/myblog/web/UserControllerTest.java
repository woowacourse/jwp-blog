package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int localServerPort;

    @Test
    public void 회원가입페이지() {
        webTestClient.get()
                .uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원가입() {
        String name = "testtest";
        String email = "test1@test.com";
        String password = "test123123";

        webTestClient.post()
                .uri("/signup")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/login");
    }

    @Test
    public void 회원가입에러() {
        String name = "test1212";
        String email = "test2";
        String password = "tes3";

        webTestClient.post()
                .uri("/signup")
                .header("referer", "/signup")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/signup");
    }

    @Test
    public void 로그인페이지() {
        webTestClient.get()
                .uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인() {
        String name = "test";
        String email = "test3@test.com";
        String password = "test123123";

        addUser(name, email, password);

        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/");
    }

    @Test
    public void 로그인실패() {
        String name = "test";
        String email = "test4@test.com";
        String password = "test123123";

        addUser(name, email, password);

        webTestClient.post()
                .uri("/login")
                .header("referer", "/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", "1111111111"))
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/login");
    }

    @Test
    public void 비로그인_회원리스트() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/login");
    }

    @Test
    public void 비로그인_수정_페이지() {
        webTestClient.get()
                .uri("/users/mypage-edit")
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/login");
    }

    @Test
    public void 비로그인_마이페이지_수정() {
        String name = "test5";
        String email = "test5@test.com";
        String password = "test123123";

        webTestClient.put()
                .uri("/users/mypage-edit")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/login");
    }

    @Test
    public void 비로그인_회원탈퇴() {
        webTestClient.delete()
                .uri("/users/mypage-edit")
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/login");
    }

    private void addUser(String name, String email, String password) {
        webTestClient.post()
                .uri("/signup")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange();
    }

}