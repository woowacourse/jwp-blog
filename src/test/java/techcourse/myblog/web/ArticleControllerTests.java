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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void showMainTest() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void showWritingPageTest() {
        webTestClient.get()
                .uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void createArticleTest() {
        String title = "title";
        String coverUrl = "coverUrl";
        String contents = "contents";

        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get()
                            .uri(response.getRequestHeaders().getLocation())
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .consumeWith(response2 -> {
                                String body = new String(response2.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(contents)).isTrue();
                            });
                });
    }

    @Test
    public void showArticleTest() {
        Article article = new Article("title", "coverUrl", "contents");
        articleRepository.addArticle(article);

        webTestClient.get()
                .uri("/articles/" + article.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("title")).isTrue();
                    assertThat(body.contains("coverUrl")).isTrue();
                    assertThat(body.contains("contents")).isTrue();
                });
    }

    @Test
    public void showEditPageTest() {
        Article article = new Article("title", "coverUrl", "contents");
        articleRepository.addArticle(article);

        webTestClient.get()
                .uri("/articles/" + article.getId() + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(article.getTitle())).isTrue();
                    assertThat(body.contains(article.getCoverUrl())).isTrue();
                    assertThat(body.contains(article.getContents())).isTrue();
                });
    }

    @Test
    public void editArticleTest() {
        Article article = new Article("title", "coverUrl", "contents");
        articleRepository.addArticle(article);

        String title = "newTitle";
        String coverUrl = "newCoverUrl";
        String contents = "newContents";

        webTestClient.put()
                .uri("/articles/" + article.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound();

        assertThat(articleRepository.findArticleById(article.getId()).getTitle()).isEqualTo(title);
        assertThat(articleRepository.findArticleById(article.getId()).getCoverUrl()).isEqualTo(coverUrl);
        assertThat(articleRepository.findArticleById(article.getId()).getContents()).isEqualTo(contents);
    }

    @Test
    public void deleteArticleTest() {
        Article article = new Article("title", "coverUrl", "contents");
        articleRepository.addArticle(article);

        webTestClient.delete()
                .uri("/articles/" + article.getId())
                .exchange()
                .expectStatus().isFound();

        assertThatThrownBy(() -> {
            articleRepository.findArticleById(article.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
