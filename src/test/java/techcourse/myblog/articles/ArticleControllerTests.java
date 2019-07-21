package techcourse.myblog.articles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ArticleService articleService;

    private Article article;

    @BeforeEach
    void setUp() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";
        Article article = Article.builder()
                .title(title)
                .coverUrl(coverUrl)
                .contents(contents)
                .build();

        this.article = articleService.save(article);
    }

    @Test
    void writeForm() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeTest() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/.*");
    }

    @Test
    void showTest() {
        webTestClient.get().uri("/articles/" + article.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(article.getTitle())).isTrue();
            assertThat(body.contains(article.getCoverUrl())).isTrue();
            assertThat(body.contains(article.getContents())).isTrue();
        });
    }

    @Test
    void editFormTest() {
        webTestClient.get().uri("/articles/" + article.getId() + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(article.getTitle())).isTrue();
            assertThat(body.contains(article.getCoverUrl())).isTrue();
            assertThat(body.contains(article.getContents())).isTrue();
        });
    }

    @Test
    void editTest() {
        String title = "1";
        String coverUrl = "2";
        String contents = "3";

        webTestClient.put().uri("/articles/" + article.getId())
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/" + article.getId());
    }

    @Test
    void deleteTest() {
        webTestClient.delete().uri("/articles/" + article.getId())
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }
}