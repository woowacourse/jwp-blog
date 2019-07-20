package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class LoginControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void 로그인_페이지_테스트() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 로그인_테스트() {
        /** 회원 가입 **/
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "코니코니")
                        .with("email", "이메일")
                        .with("password", "비번비번"))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    /** 로그인 **/
                    webTestClient.post()
                            .uri("/login")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .body(BodyInserters
                                    .fromFormData("email", "이메일이메일")
                                    .with("password", "비번비번"))
                            .exchange()
                            .expectStatus().isFound()
                            .expectBody()
                            .consumeWith(r -> {
                                /** 인덱스 페이지 이동 **/
                                webTestClient.get()
                                        .uri(r.getRequestHeaders().getLocation())
                                        .exchange()
                                        .expectStatus()
                                        .isOk();
                            });
                });
    }
}
