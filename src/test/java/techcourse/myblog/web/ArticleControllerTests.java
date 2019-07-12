package techcourse.myblog.web;

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
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void resetArticleRepository() {
        articleRepository.deleteAll();

        String title = "스파이더맨 보고싶다";
        String coverUrl = "https://pgnqdrjultom1827145.cdn.ntruss.com/img/bc/30/bc30f170793e5342c4ca6cca771da57f922f8a9a25fa09eb2b672962cda1ea92_v1.jpg";
        String contents = "스파이더맨과 미슽테리우스";

        Article article = new Article();
        article.setTitle(title);
        article.setCoverUrl(coverUrl);
        article.setContents(contents);

        articleRepository.save(article);

    }

    @Test
    void 게시글_생성_내용() {
        String title = "스파이더맨 보고싶다";
        String coverUrl = "https://pgnqdrjultom1827145.cdn.ntruss.com/img/bc/30/bc30f170793e5342c4ca6cca771da57f922f8a9a25fa09eb2b672962cda1ea92_v1.jpg";
        String contents = "스파이더맨과 미슽테리우스";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(redirectResponse -> {
                    webTestClient.get()
                            .uri(redirectResponse.getResponseHeaders().get("Location").get(0))
                            .exchange()
                            .expectBody()
                            .consumeWith(response -> {
                                String body = new String(response.getResponseBody());
                                System.out.println(body);
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();

                            });
                });
    }

    @Test
    void 게시글_생성() {
        webTestClient.post().uri("/articles")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 게시글_조회() {
        webTestClient.get().uri("/articles/0")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글_수정_시작() {
        webTestClient.get().uri("/articles/0/edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글_수정_완료() {
        webTestClient.put().uri("/articles/0")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 게시글_삭제() {
        articleRepository.deleteAll();

        webTestClient.get().uri("/articles/0")
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void 기본_페이지() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

}
