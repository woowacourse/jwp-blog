package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
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
    void createArticle() {
        webTestClient.post().uri("/articles")
                .body(fromFormData("", "").with("", "").with("", ""))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("location", ".*/articles/[0-9]{0,}");
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
        webTestClient.delete().uri("/articles/" + article.getId())
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void deleteArticle_존재하지_않는_글_삭제_fail() {
        webTestClient.delete().uri("/articles/" + 999)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void updateArticle() {
        Article updateArticle = new Article("update title", "update url", "update content");
        webTestClient.put().uri("/articles/" + article.getId())
                .body(fromFormData("title", updateArticle.getTitle())
                        .with("coverUrl", updateArticle.getCoverUrl())
                        .with("contents", updateArticle.getContents()))
                .exchange()
                .expectStatus().is3xxRedirection();

        Article updated = articleRepository.getArticleById(article.getId())
                .orElseThrow(IllegalArgumentException::new);
        assertThat(updateArticle.getTitle()).isEqualTo(updated.getTitle());
        assertThat(updateArticle.getCoverUrl()).isEqualTo(updated.getCoverUrl());
        assertThat(updateArticle.getContents()).isEqualTo(updated.getContents());
    }

    @Test
    void editArticle() {
        webTestClient.get().uri("/articles/" + article.getId() + "/edit")
                .exchange()
                .expectStatus().isOk();
    }
}
