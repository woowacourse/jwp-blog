package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void categoryGet() {
        webTestClient.get().uri("/category")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void categoryPost() {
        webTestClient.post().uri("/category")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}
