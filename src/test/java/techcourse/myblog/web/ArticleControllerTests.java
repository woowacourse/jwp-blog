package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void create_article() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches("location", ".*/articles/.*");
    }

    @Test
    void read_article() {
        String title = "blogTitle";
        String coverUrl = "blogCoverUrl";
        String contents = "blogContents";

        Article article = Article.of(title, coverUrl, contents);
        int articleId = articleRepository.insertArticle(article);


        webTestClient.get()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }

    @Test
    void read_article_edit_page() {
        String title = "blogTitle";
        String coverUrl = "blogCoverUrl";
        String contents = "blogContents";

        Article article = Article.of(title, coverUrl, contents);
        int articleId = articleRepository.insertArticle(article);

        webTestClient.get()
                .uri("/articles/" + articleId + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }

    @Test
    void update_article() {
        String title = "blogTitle";
        String coverUrl = "blogCoverUrl";
        String contents = "blogContents";

        Article article = Article.of(title, coverUrl, contents);
        int articleId = articleRepository.insertArticle(article);

        webTestClient.put()
                .uri("/articles/" + articleId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", article.getTitle())
                        .with("coverUrl", article.getCoverUrl())
                        .with("contents", article.getContents()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    // 또다시 get 을 하지 않고 이런 식으로 테스트를 해도 괜찮을까요?
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }

    @Test
    void delete_article() {
        Article article = Article.of("title", "url", "contents");
        int articleId = articleRepository.insertArticle(article);

        webTestClient.delete()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader()
                .valueMatches("location", "[^/]*//[^/]*/");
    }
}
