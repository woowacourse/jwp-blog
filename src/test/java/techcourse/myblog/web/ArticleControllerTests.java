package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.web.dto.ArticleDto;

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

    private ArticleDto article;
    private long savedId;

    @BeforeEach
    void setUp() {
        article = new ArticleDto("title1", "url1", "content1");
        savedId = articleRepository.saveArticle(article);
        articleRepository.saveArticle(new ArticleDto("title3", "url3", "content3"));
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
        webTestClient.get().uri("/articles/" + savedId)
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
        webTestClient.delete().uri("/articles/" + savedId)
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
        ArticleDto updateArticle = new ArticleDto("update title", "update url", "update content");
        webTestClient.put().uri("/articles/" + savedId)
                .body(fromFormData("title", updateArticle.getTitle())
                        .with("coverUrl", updateArticle.getCoverUrl())
                        .with("contents", updateArticle.getContents()))
                .exchange()
                .expectStatus().is3xxRedirection();

        Article updated = articleRepository.getArticleById(savedId)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(updateArticle.getTitle()).isEqualTo(updated.getTitle());
        assertThat(updateArticle.getCoverUrl()).isEqualTo(updated.getCoverUrl());
        assertThat(updateArticle.getContents()).isEqualTo(updated.getContents());
    }

    @Test
    void editArticle() {
        webTestClient.get().uri("/articles/" + savedId + "/edit")
                .exchange()
                .expectStatus().isOk();
    }
}
