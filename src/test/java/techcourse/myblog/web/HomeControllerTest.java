package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeControllerTest {
    private final WebTestClient webTestClient;

    @Autowired
    public HomeControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void 메인화면_조회() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }
}