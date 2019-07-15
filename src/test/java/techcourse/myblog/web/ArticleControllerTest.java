package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
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
public class ArticleControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ArticleRepository articleRepository;

    private final String title = "안녕슬로스";
    private final String contents = "쩌싀워더펑여우";
    private final String coverUrl = "/images/article/sloth.jpg";

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 작성된_게시글을_리스트에_등록하는지_테스트() {
        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("articleId", "1")
                        .with("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange();
        Article expected = new Article(1L, title, contents, coverUrl);
        assertThat(articleRepository.findArticleById2(1L)).isEqualTo(expected);
    }

    @Test
    void 게시물_작성_테스트() {
        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(response.getResponseHeaders().getLocation())
                            .exchange()
                            .expectStatus()
                            .isOk()
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
    void 수정할_게시물의_내용_출력_확인_테스트() {
        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(response.getResponseHeaders().getLocation() + "/edit")
                            .exchange()
                            .expectStatus()
                            .isOk()
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
    void 게시글_삭제_테스트() {
        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("articleId", "1")
                        .with("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.delete().uri(response.getResponseHeaders().getLocation())
                            .exchange()
                            .expectStatus()
                            .isFound();

                    assertThat(articleRepository.findArticleById2(1L)).isNull();
                });
    }

    @AfterEach
    void tearDown() {
        articleRepository.findAll().clear();
    }
}
