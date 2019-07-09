package techcourse.myblog.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    public void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void showArticleById() {
        addArticle();

        webTestClient.get().uri("/articles/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void showWritingPage() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void addArticle() {
        String title = "TEST";
        String postUrl = "aaa";
        String articleContents = "testtest";
        Article article = new Article(0L, title, postUrl, articleContents);

        webTestClient.post()
                .uri("/write")
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void articleForm() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }
}
