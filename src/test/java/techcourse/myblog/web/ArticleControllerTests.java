package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.Article;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void writeForm() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void show() {
        webTestClient.get().uri("/articles/2")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void write() {
        Article article = new Article(1L,"title", "coverUrl", "content");

        webTestClient.post()
                .uri("/articles")
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void delete(){
        webTestClient.delete()
                .uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void edit() {
        webTestClient.put()
                .uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void editForm() {
        webTestClient.get()
                .uri("/articles/2/edit")
                .exchange()
                .expectStatus().isOk();
    }
}
