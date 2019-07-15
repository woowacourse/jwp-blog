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
import techcourse.myblog.domain.ArticleVO;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class ArticleControllerTests {

    private ArticleRepository articleRepository;
    private String title;
    private String coverUrl;
    private String contents;
    private Article article;

    @Autowired
    public ArticleControllerTests(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

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
    void index() {
        checkIsOk(getResponse("/"));
    }

    @Test
    void writeArticle() {
        checkIsOk(getResponse("/writing"));
    }


    @Test
    void create_article() {
        ArticleVO articleVO = new ArticleVO(title, coverUrl, contents);
        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.post()
                .uri("/articles"), articleVO)
                .expectStatus().isFound();
        checkBody(responseSpec, articleVO);
    }


    @Test
    void create_update() {
        articleRepository.save(article);

        checkIsOk(getResponse("articles/" + article.getArticleId() + "/edit"));
    }


    @Test
    void submit_update() {
        ArticleVO articleVO = new ArticleVO("update title", "update coverUrl", "update contents");
        articleRepository.save(article);

        WebTestClient.ResponseSpec responseSpec = getResponse(webTestClient.put().uri("/articles/" + article.getArticleId()), articleVO);
        checkIsFound(responseSpec);
        checkBody(responseSpec, articleVO);

    }

    @Test
    void create_delete() {
        articleRepository.save(article);

        webTestClient.delete().uri("delete/articles/" + article.getArticleId())
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

    private WebTestClient.ResponseSpec getResponse(WebTestClient.RequestBodySpec requestBodySpec, ArticleVO articleVO) {
        return requestBodySpec
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", articleVO.getTitle())
                        .with("coverUrl", articleVO.getCoverUrl())
                        .with("contents", articleVO.getContents()))
                .exchange();
    }

    private void checkBody(WebTestClient.ResponseSpec responseSpec, ArticleVO articleVO) {
        responseSpec.expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(Objects.requireNonNull(response.getResponseHeaders().get("Location")).get(0))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(res -> {
                                String body = new String(Objects.requireNonNull(res.getResponseBody()));
                                assertThat(body.contains(articleVO.getTitle())).isTrue();
                                assertThat(body.contains(articleVO.getCoverUrl())).isTrue();
                                assertThat(body.contains(articleVO.getContents())).isTrue();
                            });
                });
    }


}
