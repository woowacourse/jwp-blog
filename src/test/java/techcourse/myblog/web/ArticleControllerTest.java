package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static final int TEST_ARTICLE_ID = 1;
    private static final int DELETE_TEST_ARTICLE_ID = 2;

    private Article article;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        article = new Article(
                TEST_ARTICLE_ID,
                "test title",
                "test coverUrl",
                "test contents"
        );

        articleRepository.save(article);
    }

    @Test
    @DisplayName("index 페이지를 되돌려준다.")
    void indexTest() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("게시글을 작성한 뒤 생성 버튼을 눌렀을 때 생성된 게시글을 보여준다.")
    void createNewArticleTest() {
        String inputTitle = "test title";
        String inputCoverUrl = "test coverUrl";
        String intputContents = "test contents";

        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("utf-8")
                .body(BodyInserters
                        .fromFormData("title", inputTitle)
                        .with("coverUrl", inputCoverUrl)
                        .with("contents", intputContents))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(inputTitle);
                    assertThat(body).contains(inputCoverUrl);
                    assertThat(body).contains(intputContents);
                });
    }

    @Test
    @DisplayName("새로운 Article 생성시 article-edit 페이지를 되돌려준다.")
    void articleCreationPageTest() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("게시글에서 수정 버튼을 누르는 경우 id에 해당하는 edit 페이지를 되돌려준다.")
    void articleEditPageTest() {
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
    @DisplayName("게시글을 삭제한다.")
    void deleteArticleTest() {
        Article deleteArticle = new Article(
                DELETE_TEST_ARTICLE_ID,
                "deleting title",
                "deleting coverUrl",
                "deleting contents"
        );
        articleRepository.save(deleteArticle);
        webTestClient.delete()
                .uri("/articles/" + DELETE_TEST_ARTICLE_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).doesNotContain(deleteArticle.getTitle());
                    assertThat(body).doesNotContain(deleteArticle.getCoverUrl());
                    assertThat(body).doesNotContain(deleteArticle.getContents());
                });
    }

    @AfterEach
    void tearDown() {
        articleRepository.delete(TEST_ARTICLE_ID);
    }
}
