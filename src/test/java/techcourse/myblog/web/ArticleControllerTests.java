package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
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
    private static final int TEST_ARTICLE_ID = 1;

    private Article article;
    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        article = new Article();
        article.setId();
        article.setTitle("title");
        article.setCoverUrl("coverUrl");
        article.setContents("contents");

        articleRepository.addArticle(article);
    }

    @Test
    public void showMainTest() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void showWritingPageTest() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void createArticleTest() {
        int id = 2;
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
                .expectStatus().isFound();

        assertThat(articleRepository.findArticleById(id).getTitle()).isEqualTo(title);
        assertThat(articleRepository.findArticleById(id).getCoverUrl()).isEqualTo(coverUrl);
        assertThat(articleRepository.findArticleById(id).getContents()).isEqualTo(contents);
    }

    @Test
    public void showArticleTest() {
        webTestClient.get().uri("/articles/" + TEST_ARTICLE_ID)
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
        webTestClient.get()
                .uri("/articles/" + TEST_ARTICLE_ID + "/edit")
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
        String title = "newTitle";
        String coverUrl = "newCoverUrl";
        String contents = "newContents";

        webTestClient.put()
                .uri("/articles/" + TEST_ARTICLE_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound();

        assertThat(articleRepository.findArticleById(TEST_ARTICLE_ID).getTitle()).isEqualTo(title);
        assertThat(articleRepository.findArticleById(TEST_ARTICLE_ID).getCoverUrl()).isEqualTo(coverUrl);
        assertThat(articleRepository.findArticleById(TEST_ARTICLE_ID).getContents()).isEqualTo(contents);
    }

    @Test
    public void deleteArticleTest() {
        webTestClient.delete()
                .uri("/articles/" + TEST_ARTICLE_ID)
                .exchange()
                .expectStatus().isFound();

        assertThatThrownBy(() -> {
            articleRepository.findArticleById(TEST_ARTICLE_ID);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
