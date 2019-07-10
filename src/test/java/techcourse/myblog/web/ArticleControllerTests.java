package techcourse.myblog.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.Article;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void write_POST() {
        // TODO: 2019-07-10 test...
        String title = "title";
        String content = "contents";
        String coverUrl = "https://abc.com";

        Article article = new Article(title, content, coverUrl);


        webTestClient.post()
                .uri("/articles")
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus()
                .isOk();


    }
}
