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
import techcourse.myblog.domain.ArticleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
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
    void writeArticleForm() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void saveArticle() {
        Article article = new Article();
        article.setTitle("제목");
        article.setCoverUrl("http");
        article.setContents("내용");

        webTestClient.post().uri("/articles")
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void fetchArticle() {
        Article article = new Article();
        article.setTitle("제목");
        article.setCoverUrl("http");
        article.setContents("내용");

        webTestClient.post().uri("/articles")
                .body(Mono.just(article), Article.class)
                .exchange();

        webTestClient.get().uri("/articles/0")
                .exchange()
                .expectStatus().isOk();
    }
}
