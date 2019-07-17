package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void addCategory() {
        webTestClient.post()
                .uri("/categories/add")
                .body(BodyInserters.fromFormData("categoryName", "java"))
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void deleteCategories() {
        addCategory();
        webTestClient.get()
                .uri("/categories/delete/1")
                .exchange()
                .expectStatus()
                .isFound();
    }
}