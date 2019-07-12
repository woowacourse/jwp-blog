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
import techcourse.myblog.domain.validator.CouldNotFindArticleIdException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static final int TEST_ARTICLE_ID = 1;
    private static final int DELETE_TEST_ARTICLE_ID = 0;

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
        // Given
        int expectedIdGeneratedByServer = TEST_ARTICLE_ID + 1;
        String inputTitle = "test title";
        String inputCoverUrl = "test coverUrl";
        String inputContents = "test contents";

        // When, Then
        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", inputTitle)
                        .with("coverUrl", inputCoverUrl)
                        .with("contents", inputContents))
                .exchange()
                .expectStatus().isFound();

        assertThat(articleRepository.find(expectedIdGeneratedByServer).getTitle())
                .isEqualTo(inputTitle);
        assertThat(articleRepository.find(expectedIdGeneratedByServer).getCoverUrl())
                .isEqualTo(inputCoverUrl);
        assertThat(articleRepository.find(expectedIdGeneratedByServer).getContents())
                .isEqualTo(inputContents);
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
        // Given
        Article deleteArticle = new Article(
                DELETE_TEST_ARTICLE_ID,
                "deleting title",
                "deleting coverUrl",
                "deleting contents"
        );

        articleRepository.save(deleteArticle);

        // When, Then
        webTestClient.delete()
                .uri("/articles/" + DELETE_TEST_ARTICLE_ID)
                .exchange()
                .expectStatus().isFound();

        assertThatThrownBy(() -> articleRepository.find(DELETE_TEST_ARTICLE_ID))
                .isInstanceOf(CouldNotFindArticleIdException.class);
    }

    @AfterEach
    void tearDown() {
        articleRepository.delete(TEST_ARTICLE_ID);
    }
}
