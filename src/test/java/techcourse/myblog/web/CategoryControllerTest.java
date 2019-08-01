package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void addDeleteCategory() {
        webTestClient.post()
                .uri("/categories/add")
                .body(BodyInserters.fromFormData("categoryName", "java"))
                .exchange()
                .expectStatus()
                .isFound();

        webTestClient.get()
                .uri("/categories/delete/1")
                .exchange()
                .expectStatus()
                .isFound();
    }
}