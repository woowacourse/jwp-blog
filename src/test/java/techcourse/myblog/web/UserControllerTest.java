package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 회원가입_페이지_접근_테스트() {
        webTestClient.get().uri("/signup")
                            .exchange()
                            .expectStatus()
                            .isOk();
    }

    @Test
    void 회원_등록_테스트() {
        webTestClient.post().uri("/users")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .body(
                                    BodyInserters.fromFormData("name", "donut")
                                                .with("email", "donut@woowa.com")
                                                .with("password", "qwer1234")
                            ).exchange()
                            .expectStatus()
                            .is3xxRedirection();
    }

    @Test
    void 로그인_페이지_접근_테스트() {
        webTestClient.get().uri("/login")
                            .exchange()
                            .expectStatus()
                            .isOk();
    }

    @Test
    void 회원_목록_페이지_접근_테스트() {
        webTestClient.get().uri("/users")
                            .exchange()
                            .expectStatus()
                            .isOk();
    }
}