package techcourse.myblog.controller.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }
}