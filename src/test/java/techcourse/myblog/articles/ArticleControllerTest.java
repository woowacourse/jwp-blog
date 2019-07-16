package techcourse.myblog.articles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ArticleService articleService;

    Article newArticle;

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

        newArticle = articleService.save(article);
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
        webTestClient.get().uri("/articles/" + newArticle.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(newArticle.getTitle())).isTrue();
            assertThat(body.contains(newArticle.getCoverUrl())).isTrue();
            assertThat(body.contains(newArticle.getContents())).isTrue();
        });
    }

    @Test
    void editFormTest() {
        webTestClient.get().uri("/articles/" + newArticle.getId() + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(newArticle.getTitle())).isTrue();
            assertThat(body.contains(newArticle.getCoverUrl())).isTrue();
            assertThat(body.contains(newArticle.getContents())).isTrue();
        });
    }


}