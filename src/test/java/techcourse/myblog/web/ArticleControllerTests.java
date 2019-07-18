package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.web.dto.ArticleRequestDto;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

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
        webTestClient.get().uri("/writing")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void createArticlePost() {
        Article newArticle = Article.of("hello", "http://asdf.com", "helloContent");

        postArticle(newArticle, postResponse -> {
            assertThat(postResponse.getResponseHeaders().containsKey("Location")).isTrue();
        });
    }

    @Test
    void retrieveArticle() {
        Article newArticle = Article.of("title", "http://background.com", "## some awesome contents");

        postArticle(newArticle, postResponse -> {
            retrieveArticle(postResponse.getResponseHeaders().get("Location").get(0), retrieveResponse -> {
                String body = new String(retrieveResponse.getResponseBody());
                assertThat(body)
                    .contains(newArticle.getTitle())
//                    .contains(newArticle.getCoverUrl())
                    .contains(newArticle.getContents());
            });
        });
    }

    void postArticle(Article article, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
            .uri("/articles")
            .body(BodyInserters
                .fromFormData("title", article.getTitle())
                .with("coverUrl", article.getCoverUrl())
                .with("contents", article.getContents()))
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectBody()
            .consumeWith(consumer);
    }

    void retrieveArticle(String uri, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(consumer);
    }

    @Test
    void editArticle() {
        Article newArticle = Article.of("title", "http://background.com", "가나다라마바사");

        postArticle(newArticle, postResponse -> {
            String[] splitted = postResponse.getResponseHeaders().get("Location").get(0).split("/");
            long id = Long.valueOf(splitted[splitted.length - 1]);
            webTestClient.get()
                .uri("/articles/" + id + "/edit")
                .exchange()
                .expectStatus().isOk();
        });
    }


    @Test
    void editArticlePut() {
        Article newArticle = Article.of("my article", "http://image.com/", "origin contents");
        ArticleRequestDto changedArticle = ArticleRequestDto.of("changed title", "backgroundURL", "changed contents");

        postArticle(newArticle, postResponse -> {
            String[] splitted = postResponse.getResponseHeaders().get("Location").get(0).split("/");
            long id = Long.valueOf(splitted[splitted.length - 1]);

            webTestClient.put()
                .uri("/articles/" + id)
                .body(BodyInserters
                    .fromFormData("title", changedArticle.getTitle())
                    .with("coverUrl", changedArticle.getCoverUrl())
                    .with("contents", changedArticle.getContents()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(retrieveResponse -> {
                    String body = new String(retrieveResponse.getResponseBody());
                    assertThat(body)
                        .contains(changedArticle.getTitle())
//                    .contains(newArticle.getCoverUrl())
                        .contains(changedArticle.getContents());
                });
        });
    }

    @Test
    void deleteArticle() {
        Article newArticle = Article.of("my article", "http://image.com/", "origin contents");

        postArticle(newArticle, postResponse -> {
            String[] splitted = postResponse.getResponseHeaders().get("Location").get(0).split("/");
            long id = Long.valueOf(splitted[splitted.length - 1]);
            webTestClient.delete()
                .uri("/articles/" + id)
                .exchange()
                .expectStatus().is3xxRedirection();
        });
    }
}
