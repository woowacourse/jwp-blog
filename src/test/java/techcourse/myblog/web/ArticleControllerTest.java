package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.core.test.AcceptanceBaseTest;
import techcourse.core.test.TsWebClientTemplate;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleControllerTest extends AcceptanceBaseTest {
    @Autowired
    private ArticleRepository articleRepository;

    private final String title = "안녕슬로스";
    private final String contents = "쩌싀워더펑여우";
    private final String coverUrl = "/images/article/sloth.jpg";

    @Test
    void index() {
        anonymousClient().get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        anonymousClient().get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 작성된_게시글을_리스트에_등록하는지_테스트() {
        anonymousClient().post()
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
            createResource()
            .expectBody()
            .consumeWith(response -> {
                getResource(response.getResponseHeaders().getLocation())
                        .expectBody()
                        .consumeWith(
                                res -> assertArticle(new String(res.getResponseBody()))
                        );
            });
    }

    private void assertArticle(String body) {
        assertThat(body.contains(title)).isTrue();
        assertThat(body.contains(coverUrl)).isTrue();
        assertThat(body.contains(contents)).isTrue();
    }

    private WebTestClient.ResponseSpec createResource() {
        return TsWebClientTemplate.urlEncodedForm(anonymousClient())
                    .addParam("title", title)
                    .addParam("coverUrl", coverUrl)
                    .addParam("contents", contents)
                    .formPost("/write");
    }

    private WebTestClient.ResponseSpec getResource(URI location) {
        return anonymousClient().get().uri(location)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 수정할_게시물의_내용_출력_확인_테스트() {
        anonymousClient().post()
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
                    anonymousClient().get().uri(response.getResponseHeaders().getLocation() + "/edit")
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
        anonymousClient().post()
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
                    anonymousClient().delete().uri(response.getResponseHeaders().getLocation())
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
