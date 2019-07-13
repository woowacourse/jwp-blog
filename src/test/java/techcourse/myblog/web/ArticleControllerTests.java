package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private String title;
    private String coverUrl;
    private String contents;

    @BeforeEach
    void setUp() {
        this.title = "title";
        this.coverUrl = "coverUrl";
        this.contents = "contents";
    }

    @Test
    void index() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void writeArticle() {
        webTestClient.get()
                .uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void create_article() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response->{
                    String body = new String(response.getResponseBody());
                    assertTrue(body.contains(title));
                    assertTrue(body.contains(coverUrl));
                });
    }

    @Test
    void find_article_by_Id() {

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.get()
                .uri("/articles/1")
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
    void article_edit_test() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection();

        String editedTitle="editedTitle";
        String editedCoverUrl="editedCoverUrl";
        String editedContents="editedContents";

        webTestClient.put()
                .uri("/articles/1")
                .body(BodyInserters.fromFormData("title",editedTitle)
                        .with("coverUrl",editedCoverUrl)
                        .with("contents",editedContents))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void delete_article(){
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection();

        webTestClient.delete()
                .uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

}
