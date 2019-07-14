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
        getExchange("/");
        isStatusOk();
    }

    @Test
    void articleForm() {
        getExchange("/articles/new");
        isStatusOk();
    }

    @Test
    void writeArticleForm() {
        getExchange("/writing");
        isStatusOk();
    }

    @Test
    void saveArticle() {
        postDefaultArticleExchange();
        isStatusFound();
    }

    @Test
    void fetchArticle() {
        getExchange("/articles/1");
        isStatusOk();
    }

    @Test
    void editArticle() {
        getExchange("/articles/1/edit");
        isStatusOk();
    }

    @Test
    void saveEditedArticle() {
        putExchange("/articles/1");
        isStatusOk();
    }

    @Test
    void deleteArticle() {
        postDefaultArticleExchange();

        deleteExchange("/articles/2");
        isStatusFound();
    }

    private void getExchange(String uri) {
        responseSpec = webTestClient.get().uri(uri).exchange();
    }

    private void putExchange(String uri) {
        responseSpec =  webTestClient.put().uri(uri).exchange();
    }

    private void deleteExchange(String uri) {
        responseSpec =  webTestClient.delete().uri(uri).exchange();
    }

    private void postDefaultArticleExchange() {
        responseSpec =  webTestClient.post().uri("/articles")
                .body(Mono.just(article), Article.class)
                .exchange();
    }

    private void isStatusOk() {
        responseSpec.expectStatus().isOk();
    }

    private void isStatusFound() {
        responseSpec.expectStatus().isFound();
    }

    @AfterEach
    void tearDown() {
        article = null;
        responseSpec = null;
    }
}
