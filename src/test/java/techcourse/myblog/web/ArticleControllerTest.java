package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    void 로그인_전_Article_생성_페이지_접근() {
        webTestClient.get().uri("/articles/new").exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_생성_페이지_접근() {
        String jSessionId = getJSessionId("jun", "jun@gmail.com", "Aa12345!");
        webTestClient.get().uri("/articles/new")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_전_Article_생성() {
        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);
        getResponse(webTestClient.post()
                .uri("/articles/new"), articleDto, null)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_생성() {
        String jSessionId = getJSessionId("Buddy","buddy@gmail.com","Aa12345!");
        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);

        WebTestClient.RequestBodySpec requestBodySpec = webTestClient.post().uri("/articles/new")
                .cookie("JSESSIONID", jSessionId);

        WebTestClient.ResponseSpec responseSpec = getResponse(requestBodySpec, articleDto, jSessionId)
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles.*");

        checkBody(responseSpec,articleDto);
    }

    //TODO
//    @Test
//    void 없는_Article() {
//        webTestClient.get().uri("/articles/10").exchange()
//                .expectStatus().isBadRequest();
//    }

    @Test
    void 로그인_전_수정_페이지_접근() {
        articleRepository.save(article);
        webTestClient.get().uri("articles/" + article.getArticleId() + "/edit")
                .exchange().expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_수정_페이지_접근() {
        Article article = articleRepository.save(this.article);
        String jSessionId = getJSessionId("coni", "coni@gmail.com", "Aa12345!");
        webTestClient.get().uri("articles/" + article.getArticleId() + "/edit")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_전_Article_수정() {
        Article article = articleRepository.save(this.article);
        ArticleDto articleDto = new ArticleDto("update title", "update coverUrl", "update contents");

        getResponse(webTestClient.put().uri("/articles/" + article.getArticleId()), articleDto, null)
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_Article_수정() {
        Article article = articleRepository.save(this.article);
        ArticleDto articleDto = new ArticleDto("update title", "update coverUrl", "update contents");
        String jSessionId = getJSessionId("hwatu", "hawtu@gmail.com", "Aa12345!");

        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.put().uri("/articles/"
                + article.getArticleId()), articleDto, jSessionId);

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

    @Test
    void 로그인_전_Article_삭제() {
        Article article = articleRepository.save(this.article);

        webTestClient.delete().uri("/articles/" + article.getArticleId())
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_Article_삭제() {
        Article article = articleRepository.save(this.article);

        webTestClient.delete().uri("/articles/" + article.getArticleId())
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/.*");
    }

    private WebTestClient.ResponseSpec getResponse(WebTestClient.RequestBodySpec requestBodySpec, ArticleDto articleDto, String jSessionId) {
        return requestBodySpec
                .cookie("JSESSIONID", jSessionId)
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
