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
    private static final String ROOT_URL = "/";
    private static final String EDIT_URL = "/articles/1/edit";
    private static final String WRITING_URL = "/writing";
    private static final String ARTICLE_URL = "/articles";
    private static final String SPECIFIC_ARTICLE_URL = "/articles/1";

    private static final String ARTICLE_DELIMITER
            = "<div role=\"tabpanel\" class=\"tab-pane fade in active\" id=\"tab-centered-1\">";
    private static final String TITLE_NAME = "title";
    private static final String COVER_URL_NAME = "coverUrl";
    private static final String CONTENTS_NAME = "contents";
    private static final String TITLE_VALUE = "TEST";
    private static final String COVER_URL_VALUE = "https://img.com";
    private static final String CONTENTS_VALUE = "testtest";
    private static final Article ARTICLE_SAMPLE;

    static {
        ARTICLE_SAMPLE = new Article();
        ARTICLE_SAMPLE.setTitle(TITLE_VALUE);
        ARTICLE_SAMPLE.setCoverUrl(COVER_URL_VALUE);
        ARTICLE_SAMPLE.setContents(CONTENTS_VALUE);
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
        addArticle();
        addArticle();
        addArticle();

        webTestClient.get()
                .uri(ROOT_URL)
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
        addArticle();

        webTestClient.get()
                .uri(SPECIFIC_ARTICLE_URL)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateArticleById() {
        addArticle();

        webTestClient.get()
                .uri(EDIT_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(TITLE_VALUE)).isTrue();
                    assertThat(body.contains(COVER_URL_VALUE)).isTrue();
                    assertThat(body.contains(CONTENTS_VALUE)).isTrue();
                });
    }

    @Test
    public void showWritingPage() {
        webTestClient.get()
                .uri(WRITING_URL)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void addArticle() {
        webTestClient.post()
                .uri(ARTICLE_URL)
                .body(BodyInserters
                        .fromFormData(TITLE_NAME, TITLE_VALUE)
                        .with(COVER_URL_NAME, COVER_URL_VALUE)
                        .with(CONTENTS_NAME, CONTENTS_VALUE))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    public void deleteArticle() {
        addArticle();

        webTestClient.delete()
                .uri(SPECIFIC_ARTICLE_URL)
                .exchange()
                .expectStatus().isFound();
    }

    @AfterEach
    public void tearDown() {
        articleRepository.deleteAll();
    }
}
