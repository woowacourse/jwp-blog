package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void index() {
        testGetUri("/");
    }

    @Test
    void articleForm() {
        testGetUri("/writing");
    }

    private void testGetUri(String uri) {
        webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk();
    }
}
