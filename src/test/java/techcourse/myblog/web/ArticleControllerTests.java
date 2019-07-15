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

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final String SAMPLE_TITLE = "test title";
    private static final String SAMPLE_COVER_URL = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
    private static final String SAMPLE_CONTENTS = "test contents";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showArticles() {
        webTestClient.get().uri("/articles")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showCreatePage() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createArticle() {
        String title = "목적의식 있는 연습을 통한 효과적인 학습";
        String coverUrl = "https://techcourse.woowahan.com/images/default/default-cover.jpeg";
        String contents = "helloWould";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get()
                            .uri(location)
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });
                });
    }

    @Test
    void showArticle() {
        long id = addSampleArticle();

        webTestClient.get()
                .uri("/articles/" + id)
                .exchange()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body.contains(SAMPLE_TITLE)).isTrue();
                    assertThat(body.contains(SAMPLE_COVER_URL)).isTrue();
                    assertThat(body.contains(SAMPLE_CONTENTS)).isTrue();
                });
    }

    @Test
    void showEditPage() {
        long id = addSampleArticle();

        webTestClient.get()
                .uri("/articles/" + id + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(SAMPLE_TITLE)).isTrue();
                    assertThat(body.contains(SAMPLE_COVER_URL)).isTrue();
                    assertThat(body.contains(SAMPLE_CONTENTS)).isTrue();
                });
    }

    @Test
    void editArticle() {
        String newTitle = "test";
        String newCoverUrl = "newCorverUrl";
        String newContents = "newContents";
        long id = addSampleArticle();

        webTestClient.put()
                .uri("/articles/" + id)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", newCoverUrl)
                        .with("contents", newContents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get()
                            .uri(location)
                            .exchange()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(res.getResponseBody());
                                assertThat(body.contains(newTitle)).isTrue();
                                assertThat(body.contains(newCoverUrl)).isTrue();
                                assertThat(body.contains(newContents)).isTrue();
                            });
                });
    }

    @Test
    void deleteArticle() {
        long id = addSampleArticle();

        webTestClient.delete()
                .uri("/articles/" + id)
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get()
                .uri("/articles/" + id)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    private long addSampleArticle() {
        Article article = new Article(SAMPLE_TITLE, SAMPLE_COVER_URL, SAMPLE_CONTENTS);
        return articleRepository.save(article);
    }
}
