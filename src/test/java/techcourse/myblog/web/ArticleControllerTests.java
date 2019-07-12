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

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class ArticleControllerTests {

    private ArticleRepository articleRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ArticleControllerTests(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeArticle() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void create_article() {
        String title = "sss";
        String coverUrl = "some-cover-image-url";
        String contents = "contents";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });
                });
    }

    @Test
    void create_update(){
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";
        articleRepository.save(new Article(title, coverUrl, contents));
        webTestClient.get().uri("articles/1/edit").exchange().expectStatus().isOk();
    }

    @Test
    void submit_update() {
        String title = "update title";
        String coverUrl = "update coverUrl";
        String contents = "update contents";
        articleRepository.save(new Article("title", "coverUrl", "contents"));
        webTestClient.put().uri("/articles/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                .fromFormData("title",title)
                .with("coverUrl",coverUrl)
                .with("contents",contents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });

                });

    }

    @Test
    void create_delete(){
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";
        articleRepository.save(new Article(title,coverUrl,contents));
        webTestClient.delete().uri("delete/articles/1").exchange().expectStatus().is3xxRedirection();
    }

}
