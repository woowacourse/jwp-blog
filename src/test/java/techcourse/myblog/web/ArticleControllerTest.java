package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.article.Article;
import techcourse.myblog.article.ArticleRepository;
import techcourse.myblog.service.dto.ArticleDto;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest extends AbstractControllerTest{

    @Autowired
    private ArticleRepository articleRepository;
    private String title;
    private String coverUrl;
    private String contents;
    private Article article;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        title = "title";
        coverUrl = "coverUrl";
        contents = "contents";
        article = new Article(title, coverUrl, contents);
    }

    @Test
    void Article_생성_페이지_접근() {
        checkIsOk(getResponse("/articles/writing"));
    }

    @Test
    void 로그인_전_Article_생성() {
        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);
        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.post()
                .uri("/articles"), articleDto)
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_후_Article_생성() {
        String jSessionId = getJSessionId("Buddy","buddy@gmail.com","Aa12345!");
        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);

        WebTestClient.RequestBodySpec requestBodySpec = webTestClient.post().uri("/articles")
                .cookie("JSESSIONID", jSessionId);

        WebTestClient.ResponseSpec responseSpec = getResponse(requestBodySpec, articleDto)
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");

        checkBody(responseSpec,articleDto);
    }

    @Test
    void 없는_Article() {
        webTestClient.get().uri("/articles/10").exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 수정_페이지_접근() {
        articleRepository.save(article);

        checkIsOk(getResponse("articles/" + article.getArticleId() + "/edit"));
    }

    @Test
    void Article_수정() {
        Article article = articleRepository.save(this.article);
        ArticleDto articleDto = new ArticleDto("update title", "update coverUrl", "update contents");

        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.put().uri("/articles/" + article.getArticleId()), articleDto);
        checkIsFound(responseSpec);
        checkBody(responseSpec, articleDto);

    }

    @Test
    void Article_삭제() {
        Article article = articleRepository.save(this.article);

        webTestClient.delete().uri("/articles/" + article.getArticleId())
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    private void checkIsFound(WebTestClient.ResponseSpec responseSpec) {
        responseSpec.expectStatus().isFound();
    }

    private void checkIsOk(WebTestClient.ResponseSpec responseSpec) {
        responseSpec.expectStatus().isOk();
    }

    private WebTestClient.ResponseSpec getResponse(String uri) {
        return webTestClient.get().uri(uri)
                .exchange();
    }

    private WebTestClient.ResponseSpec getResponse(WebTestClient.RequestBodySpec requestBodySpec, ArticleDto articleDto) {
        return requestBodySpec
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", articleDto.getTitle())
                        .with("coverUrl", articleDto.getCoverUrl())
                        .with("contents", articleDto.getContents()))
                .exchange();
    }

    private void checkBody(WebTestClient.ResponseSpec responseSpec, ArticleDto articleDto) {
        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(articleDto.getTitle())).isTrue();
                                assertThat(body.contains(articleDto.getCoverUrl())).isTrue();
                                assertThat(body.contains(articleDto.getContents())).isTrue();
                            });
                });
    }
}
