package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final String ARTICLE_DELIMITER
            = "<div role=\"tabpanel\" class=\"tab-pane fade in active\" id=\"tab-centered-1\">";
    private static final Article ARTICLE_SAMPLE;
    private static String title = "TEST";
    private static String coverUrl = "https://img.com";
    private static String contents = "testtest";

    static {
        ARTICLE_SAMPLE = new Article();
        ARTICLE_SAMPLE.setTitle(title);
        ARTICLE_SAMPLE.setCoverUrl(coverUrl);
        ARTICLE_SAMPLE.setContents(contents);
    }

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
    }

    @Test
    public void index() {
        final int count = 3;
        articleRepository.add(ARTICLE_SAMPLE);
        articleRepository.add(ARTICLE_SAMPLE);
        articleRepository.add(ARTICLE_SAMPLE);

        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, ARTICLE_DELIMITER));
                });


    }

    @Test
    public void showArticleById() {
        articleRepository.add(ARTICLE_SAMPLE);

        webTestClient.get().uri("/articles/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateArticleById() {
        articleRepository.add(ARTICLE_SAMPLE);

        webTestClient.get().uri("/articles/1/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }

    @Test
    public void showWritingPage() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void addArticle() {
        webTestClient.post()
                .uri("/write")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    public void deleteArticle() {
        articleRepository.add(ARTICLE_SAMPLE);

        webTestClient.delete()
                .uri("/articles/1")
                .exchange()
                .expectStatus().isFound();
    }

    @AfterEach
    public void tearDown() {
        articleRepository.deleteAll();
    }
}
