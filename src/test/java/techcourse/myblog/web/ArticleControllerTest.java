package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private Article article;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        article = new Article(0L, "제목입니다", "커버입니다", "내용입니다");
    }

    @Test
    public void 인덱스_페이지_테스트() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 게시글_작성_페이지_테스트() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    private void createArticle(Article article, Consumer<EntityExchangeResult<byte[]>> consumer) {
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

    @Test
    public void 게시글_추가_테스트() {
        createArticle(article, response -> {
            webTestClient.get()
                    .uri(response.getResponseHeaders().getLocation())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .consumeWith(res -> {
                        String body = new String(res.getResponseBody());
                        assertThat(body.contains(article.getTitle())).isTrue();
                        assertThat(body.contains(article.getCoverUrl())).isTrue();
                        assertThat(body.contains(article.getContents())).isTrue();
                    });
        });
    }

    @Test
    public void 게시글_수정_페이지_테스트() {
        createArticle(article, response -> {
            webTestClient.get()
                    .uri(response.getResponseHeaders().getLocation() + "/edit")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .consumeWith(res -> {
                        String body = new String(res.getResponseBody());
                        assertThat(body.contains(article.getTitle())).isTrue();
                        assertThat(body.contains(article.getCoverUrl())).isTrue();
                        assertThat(body.contains(article.getContents())).isTrue();
                    });
        });
    }

    @Test
    public void 게시글_수정_테스트() {
        createArticle(article, response -> {
            webTestClient.put()
                    .uri(response.getResponseHeaders().getLocation())
                    .body(BodyInserters
                            .fromFormData("title", "새로운 제목입니다")
                            .with("coverUrl", "새로운 커버입니다")
                            .with("contents", "새로운 내용입니다"))
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody()
                    .consumeWith(res -> {
                        webTestClient.get()
                                .uri(res.getResponseHeaders().getLocation())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .consumeWith(r -> {
                                    String body = new String(r.getResponseBody());
                                    assertThat(body.contains("새로운 제목입니다")).isTrue();
                                    assertThat(body.contains("새로운 커버입니다")).isTrue();
                                    assertThat(body.contains("새로운 내용입니다")).isTrue();
                                });
                    });
        });
    }

    @Test
    public void 게시글_삭제_테스트() {
        createArticle(article, response -> {
            webTestClient.delete()
                    .uri(response.getResponseHeaders().getLocation())
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody()
                    .consumeWith(res -> {
                        webTestClient.get()
                                .uri(res.getRequestHeaders().getLocation())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .consumeWith(r -> {
                                    String body = new String(r.getResponseBody());
                                    assertThat(body).doesNotContain(article.getTitle());
                                    assertThat(body).doesNotContain(article.getCoverUrl());
                                    assertThat(body).doesNotContain(article.getContents());
                                });
                    });
        });
    }
}