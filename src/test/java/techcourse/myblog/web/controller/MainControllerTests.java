package techcourse.myblog.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("메인페이지 기본 uri만 입력")
    void main() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("메인페이지 article pageSize 맞는지 확인")
    void order() {
        webTestClient.get().uri("/?order=desc&pageSize=5")
                .exchange()
                .expectStatus().isOk();
    }
}
