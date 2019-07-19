package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repo.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {

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
    void writeArticle() {
        checkIsOk(getResponse("/articles/new"));
    }


    @Test
    void create_article() {
        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);
        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.post()
                .uri("/articles"), articleDto)
                .expectStatus().isFound();
        checkBody(responseSpec, articleDto);
    }


    @Test
    void create_update() {
        articleRepository.save(article);

        checkIsOk(getResponse("articles/" + article.getId() + "/edit"));
    }


    @Test
    void submit_update() {
        articleRepository.save(article);
        ArticleDto articleDto = new ArticleDto("update title", "update coverUrl", "update contents");

        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.put().uri("/articles/" + article.getId()), articleDto);
        checkIsFound(responseSpec);
        checkBody(responseSpec, articleDto);

    }

    @Test
    void create_delete() {
        articleRepository.save(article);

        webTestClient.delete().uri("/articles/" + article.getId())
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
