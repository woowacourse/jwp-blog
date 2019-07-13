package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final String SAMPLE_TITLE = "목적의식 있는 연습을 통한 효과적인 학습";
    private static final String SAMPLE_COVER_URL = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
    private static final String SAMPLE_CONTENTS = "Test Contents";
    private static final Article sampleArticle;

    static {
        sampleArticle = new Article(SAMPLE_TITLE, SAMPLE_COVER_URL, SAMPLE_CONTENTS);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void index() {
        check200("/");
    }

    @Test
    void showArticles() {
        check200("/articles");
    }

    @Test
    void showCreatePage() {
        check200("/articles/new");
    }

    @Test
    void createArticle() {
        postArticle("/articles", sampleArticle)
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    check200WithBody(String.valueOf(location), sampleArticle);
                });
    }

    @Test
    void showArticle() {
        articleRepository.add(sampleArticle);
        check200WithBody("/articles/" + sampleArticle.getId(), sampleArticle);
    }

    @Test
    void showEditPage() {
        articleRepository.add(sampleArticle);
        check200WithBody("/articles/" + sampleArticle.getId() + "/edit", sampleArticle);
    }

    @Test
    void editArticle() {
        String newContents = "Bye";
        Article newArticle = new Article(SAMPLE_TITLE, SAMPLE_COVER_URL, newContents);

        articleRepository.add(sampleArticle);
        updateArticle("/articles/" + sampleArticle.getId(), newArticle)
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    check200WithBody(String.valueOf(location), newArticle);
                });
    }

    @Test
    void deleteArticle() {
        articleRepository.add(sampleArticle);
        deleteArticle("/articles/" + sampleArticle.getId(), sampleArticle);

        webTestClient.get()
                .uri("/articles/" + sampleArticle.getId())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    private WebTestClient.ResponseSpec check200(String uri) {
        return webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk();
    }

    private void check200WithBody(String uri, Article article) {
        check200(uri)
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(article.getTitle())).isTrue();
                    assertThat(body.contains(article.getCoverUrl())).isTrue();
                    assertThat(body.contains(article.getContents())).isTrue();
                });

    }

    private WebTestClient.ResponseSpec postArticle(String uri, Article article) {
        return webTestClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", article.getTitle())
                        .with("coverUrl", article.getCoverUrl())
                        .with("contents", article.getContents()))
                .exchange()
                .expectStatus()
                .isFound();
    }

    private WebTestClient.ResponseSpec updateArticle(String uri, Article newArticle) {
        return webTestClient.put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newArticle.getTitle())
                        .with("coverUrl", newArticle.getCoverUrl())
                        .with("contents", newArticle.getContents()))
                .exchange()
                .expectStatus().isFound();
    }

    private WebTestClient.ResponseSpec deleteArticle(String uri, Article article) {
        return webTestClient.delete()
                .uri(uri)
                .exchange()
                .expectStatus().isFound();
    }

}
