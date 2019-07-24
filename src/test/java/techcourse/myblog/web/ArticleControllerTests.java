package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.article.Article;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private FluxExchangeResult fluxExchangeResult;
    private String title;
    private String coverUrl;
    private String contents;

    @BeforeEach
    void setUp() {
        title = "title";
        coverUrl = "";
        contents = "contents";

        fluxExchangeResult = webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .returnResult(Article.class);
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri(fluxExchangeResult.getResponseHeaders().getLocation())
                .exchange();
    }

    @Test
    void 메인화면() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글_생성_페이지_이동() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글_생성() {
        EntityExchangeResult result = webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".*/articles/.")
                .expectBody()
                .returnResult();

        webTestClient.get().uri(result.getResponseHeaders().getLocation())
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body).contains(title);
                    assertThat(body).contains(coverUrl);
                    assertThat(body).contains(contents);
                });

        webTestClient.delete().uri(result.getResponseHeaders().getLocation())
                .exchange();
    }

    @Test
    void 게시글_조회() {
        webTestClient.get().uri(fluxExchangeResult.getResponseHeaders().getLocation())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body).contains(title);
                    assertThat(body).contains(coverUrl);
                    assertThat(body).contains(contents);
                });
    }

    @Test
    void 게시글_수정() {
        FluxExchangeResult result = webTestClient.put().uri(fluxExchangeResult.getResponseHeaders().getLocation())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "newTitle")
                        .with("coverUrl", coverUrl)
                        .with("contents", "newContents"))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".*/articles/.")
                .returnResult(Article.class);

        webTestClient.get().uri(result.getResponseHeaders().getLocation())
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body).contains("newTitle");
                    assertThat(body).contains(coverUrl);
                    assertThat(body).contains("newContents");
                });
    }

    @Test
    void 게시글_삭제() {
        FluxExchangeResult result = webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .returnResult(Article.class);

        webTestClient.delete().uri(result.getResponseHeaders().getLocation())
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".*/");

        webTestClient.get().uri(result.getResponseHeaders().getLocation())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void 게시글_수정_페이지_이동() {
        webTestClient.get().uri(fluxExchangeResult.getResponseHeaders().getLocation() + "/edit")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
