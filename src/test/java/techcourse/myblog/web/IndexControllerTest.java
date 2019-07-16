package techcourse.myblog.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("index.html을 제대로 되돌려준다.")
    void indexTest() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }
}