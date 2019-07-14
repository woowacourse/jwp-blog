package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.Article;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;
    private WebTestClient.ResponseSpec responseSpec;
    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article();
        article.setTitle("제목");
        article.setCoverUrl("http");
        article.setContents("내용");

        postDefaultArticleExchange();
    }

    @Test
    void index() {
        responseSpec = getExchange("/");
        isStatusOk(responseSpec);
    }

    @Test
    void articleForm() {
        responseSpec = getExchange("/articles/new");
        isStatusOk(responseSpec);
    }

    @Test
    void writeArticleForm() {
        responseSpec = getExchange("/writing");
        isStatusOk(responseSpec);
    }

    @Test
    void saveArticle() {
        responseSpec = postDefaultArticleExchange();
        isStatusFound(responseSpec);
    }

    @Test
    void fetchArticle() {
        responseSpec = getExchange("/articles/1");
        isStatusOk(responseSpec);
    }

    @Test
    void editArticle() {
        responseSpec = getExchange("/articles/1/edit");
        isStatusOk(responseSpec);
    }

    @Test
    void saveEditedArticle() {
        responseSpec = putExchange("/articles/1");
        isStatusOk(responseSpec);
    }

    @Test
    void deleteArticle() {
        postDefaultArticleExchange();

        responseSpec = deleteExchange("/articles/2");
        isStatusFound(responseSpec);
    }

    private WebTestClient.ResponseSpec getExchange(String uri) {
        return webTestClient.get().uri(uri).exchange();
    }

    private WebTestClient.ResponseSpec putExchange(String uri) {
        return webTestClient.put().uri(uri).exchange();
    }

    private WebTestClient.ResponseSpec deleteExchange(String uri) {
        return webTestClient.delete().uri(uri).exchange();
    }

    private WebTestClient.ResponseSpec postDefaultArticleExchange() {
        return webTestClient.post().uri("/articles")
                .body(Mono.just(article), Article.class)
                .exchange();
    }

    private WebTestClient.ResponseSpec isStatusOk(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec.expectStatus().isOk();
    }

    private WebTestClient.ResponseSpec isStatusFound(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec.expectStatus().isFound();
    }

    @AfterEach
    void tearDown() {
        article = null;
        responseSpec = null;
    }
}
