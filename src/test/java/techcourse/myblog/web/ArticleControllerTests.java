package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private final String mappingUrl = "/articles";

    private String title;
    private String coverUrl;
    private String contents;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        title = "title";
        coverUrl = "coverUrl";
        contents = "contents";
        webTestClient.post().uri(mappingUrl)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange();
    }

    @Test
    void create_form_page_status() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void create_redirect() {
        webTestClient.post().uri(mappingUrl)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*article.*");
    }

    @Test
    void create_data() {
        webTestClient.post()
                .uri(mappingUrl)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri(response.getRequestHeaders().getLocation())
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(response2 -> {
                                String body = new String(response2.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });
                });
    }

    @Test
    void not_found_article() {
        webTestClient.get()
                .uri(mappingUrl + "123")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void edit_form_status() {
        webTestClient.get()
                .uri(mappingUrl + "/1/edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void edit_data() {
        final String editValue = "edit";
        final int editId = 1;
        webTestClient.put()
                .uri(mappingUrl + "/" + editId)
                .body(BodyInserters
                        .fromFormData("title", editValue)
                        .with("coverUrl", editValue)
                        .with("contents", editValue))
                .exchange();

        webTestClient.get()
                .uri(mappingUrl + "/" + editId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(editValue)).isTrue();
                    assertThat(body.contains(editValue)).isTrue();
                    assertThat(body.contains(editValue)).isTrue();
                });
    }

    @Test
    void edit_data_not_found() {
        final String editValue = "edit";
        final int editId = 0;
        webTestClient.put()
                .uri(mappingUrl + "/" + 100)
                .body(BodyInserters
                        .fromFormData("title", editValue)
                        .with("coverUrl", editValue)
                        .with("contents", editValue))
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
