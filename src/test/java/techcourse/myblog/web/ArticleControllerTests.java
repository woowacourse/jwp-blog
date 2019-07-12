package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    private Article article;
    private Article deleteArticle;

    @BeforeEach
    void setUp() {
        article = new Article("title1", "url1", "content1");
        deleteArticle = new Article("title2", "url2", "content2");
        articleRepository.saveArticle(article);
        articleRepository.saveArticle(deleteArticle);
        articleRepository.saveArticle(new Article("title3", "url3", "content3"));
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createArticle() {
        webTestClient.post().uri("/articles")
                .body(fromFormData("", "").with("", "").with("", ""))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles/(\\d)*");
    }

    @Test
    void goDetailArticle() {
        webTestClient.get().uri("/articles/" + article.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(Objects.requireNonNull(response.getResponseBody()));
                    assertThat(body.contains(article.getTitle())).isTrue();
                    assertThat(body.contains(article.getCoverUrl())).isTrue();
                    assertThat(body.contains(article.getContents())).isTrue();

                });
    }

    @Test
    void deleteArticle() {
        webTestClient.delete().uri("/articles/" + deleteArticle.getId())
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void updateArticle() {
        Article updateArticle = new Article("update title", "update url", "update content");
        updateArticle.setId(article.getId());
        webTestClient.put().uri("/articles/" + article.getId())
                .body(fromFormData("title", updateArticle.getTitle())
                        .with("coverUrl", updateArticle.getCoverUrl())
                        .with("contents", updateArticle.getContents()))
                .exchange()
                .expectStatus().is3xxRedirection();

        assertThat(updateArticle.getTitle()).isEqualTo(articleRepository.getArticleById(article.getId()).getTitle());
        assertThat(updateArticle.getCoverUrl()).isEqualTo(articleRepository.getArticleById(article.getId()).getCoverUrl());
        assertThat(updateArticle.getContents()).isEqualTo(articleRepository.getArticleById(article.getId()).getContents());
    }

    @Test
    void editArticle() {
        webTestClient.get().uri("/articles/" + article.getId() + "/edit")
                .exchange()
                .expectStatus().isOk();
    }
}
