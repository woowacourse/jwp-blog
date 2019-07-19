package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void 회원등록_오류_테스트() {
        String name = "name";
        String email = "email";
        String password = "password";
        webTestClient.post()
                .uri("/join")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/err")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원등록_테스트() {
        String name = "name";
        String email = "pobi@naver.com";
        String password = "aA1231!@";
        webTestClient.post()
                .uri("/join")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/login")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원조회_테스트() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_테스트() {
        String name = "name";
        String email = "ILovePobi@naver.com";
        String password = "aA1231!@";
        webTestClient.post()
                .uri("/join")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange();

        webTestClient.post().uri("/login")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/;jsessionid=([0-9A-Z])+")
                .expectStatus().is3xxRedirection();
    }
}