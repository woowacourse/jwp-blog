package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void create_article() {
        String id = "0";
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("id", id)
                        .with("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void read_article() {
        Article article = Article.of("title", "url", "contents");
        int articleId = articleRepository.insertArticle(article);

        webTestClient.get()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void read_article_edit_page() {
        Article article = Article.of("title", "url", "contents");
        int articleId = articleRepository.insertArticle(article);

        webTestClient.get()
                .uri("/articles/" + articleId + "/edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void update_article() {
        Article article = Article.of("title", "url", "contents");
        int articleId = articleRepository.insertArticle(article);

        webTestClient.put()
                .uri("/articles/" + articleId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("id",  Integer.toString(article.getId()))
                        .with("title", article.getTitle())
                        .with("coverUrl", article.getCoverUrl())
                        .with("contents", article.getContents()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void delete_article() {
        Article article = Article.of("title", "url", "contents");
        int articleId = articleRepository.insertArticle(article);

        webTestClient.delete()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isOk();
    }
}
