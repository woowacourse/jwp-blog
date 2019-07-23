package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

import java.net.URI;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    private static Article article;

    static {
        article = Article.builder()
                .title("Title")
                .coverUrl("https://cover.com")
                .contents("Contents")
                .build();
    }

    @Test
    void showArticleEditPage() {
        webTestClient.get()
                .uri("/articles/new")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void createArticle() {
        articleWithBody(webTestClient.post(), "/articles", article, postResponse -> {
            URI location = postResponse.getResponseHeaders().getLocation();
            getArticle(location.toString(), getResponse -> checkArticleInBody(article, getResponse));
        });
    }

    @Test
    void showArticlePage() {
        articleRepository.save(article);
        getArticle("/articles/" + article.getId(),
                res -> checkArticleInBody(article, res));
    }

    @Test
    void showEditPage() {
        Article savedArticle = articleRepository.save(article);
        getArticle("/articles/" + savedArticle.getId() + "/edit",
                res -> checkArticleInBody(article, res));
    }

    @Test
    void updateArticle() {
        Article savedArticle = articleRepository.save(article);
        Article updateArticle =
                Article.builder()
                        .title("Update")
                        .coverUrl("Update URL")
                        .contents("Update Contents")
                        .build();

        articleWithBody(webTestClient.put(), "/articles/" + savedArticle.getId(), updateArticle, postResponse -> {
            URI location = postResponse.getResponseHeaders().getLocation();
            getArticle(location.toString(), getResponse -> checkArticleInBody(updateArticle, getResponse));
        });
    }

    @Test
    void deleteArticle() {
        Article savedArticle = articleRepository.save(article);
        webTestClient.delete()
                .uri("/articles/" + savedArticle.getId())
                .exchange()
                .expectStatus()
                .isFound();

        webTestClient.get()
                .uri("/articles/" + savedArticle.getId())
                .exchange()
                .expectStatus()
                .isFound();
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }

    void articleWithBody(WebTestClient.RequestBodyUriSpec requestBodyUriSpec, String uri, Article article, Consumer<EntityExchangeResult<byte[]>> consumer) {
        requestBodyUriSpec
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", article.getTitle())
                        .with("coverUrl", article.getCoverUrl())
                        .with("contents", article.getContents()))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(consumer);
    }

    void getArticle(String uri, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(consumer);
    }

    private void checkArticleInBody(Article article, EntityExchangeResult<byte[]> getResponse) {
        String body = new String(getResponse.getResponseBody());
        assertThat(body.contains(article.getTitle())).isTrue();
        assertThat(body.contains(article.getCoverUrl())).isTrue();
        assertThat(body.contains(article.getContents())).isTrue();
    }
}
