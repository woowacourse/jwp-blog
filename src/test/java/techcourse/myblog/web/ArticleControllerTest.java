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
import techcourse.myblog.domain.exception.CouldNotFindArticleIdException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private ArticleDTO articleDTO;
    private int savedArticleIdBeforeTest;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        articleDTO = new ArticleDTO(
                "test title",
                "test coverUrl",
                "test contents"
        );

        articleRepository.save(articleDTO);
        savedArticleIdBeforeTest = articleRepository.getLastGeneratedId();
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
        String inputTitle = "test title";
        String inputCoverUrl = "test coverUrl";
        String inputContents = "test contents";

        // When
        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", inputTitle)
                        .with("coverUrl", inputCoverUrl)
                        .with("contents", inputContents))
                .exchange()
                .expectStatus().isFound();

        // Then
        int lastSavedId = articleRepository.getLastGeneratedId();

        assertThat(articleRepository.findBy(lastSavedId).getTitle())
                .isEqualTo(inputTitle);
        assertThat(articleRepository.findBy(lastSavedId).getCoverUrl())
                .isEqualTo(inputCoverUrl);
        assertThat(articleRepository.findBy(lastSavedId).getContents())
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
                .uri("/articles/" + savedArticleIdBeforeTest + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(articleDTO.getTitle())).isTrue();
                    assertThat(body.contains(articleDTO.getCoverUrl())).isTrue();
                    assertThat(body.contains(articleDTO.getContents())).isTrue();
                });
    }

    @Test
    @DisplayName("게시글 수정 페이지에서 데이터들을 받아 게시글을 수정한다")
    void articleEditTest() {
        String changedTitle = "changed title";
        String changedCoverUrl = "changed coverUrl";
        String changedContents = "changed contents";

        webTestClient.put()
                .uri("/articles/" + savedArticleIdBeforeTest)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", changedTitle)
                        .with("coverUrl", changedCoverUrl)
                        .with("contents", changedContents))
                .exchange()
                .expectStatus().isFound();

        Article article = articleRepository.findBy(savedArticleIdBeforeTest);

        assertThat(article.getTitle()).isEqualTo(changedTitle);
        assertThat(article.getCoverUrl()).isEqualTo(changedCoverUrl);
        assertThat(article.getContents()).isEqualTo(changedContents);
    }

    @Test
    @DisplayName("게시글을 삭제한다.")
    void deleteArticleTest() {
        // Given
        ArticleDTO testArticleDTO = new ArticleDTO(
                "deleting title",
                "deleting coverUrl",
                "deleting contents"
        );

        articleRepository.save(testArticleDTO);

        // When, Then
        int lastSavedId = articleRepository.getLastGeneratedId();

        webTestClient.delete()
                .uri("/articles/" + lastSavedId)
                .exchange()
                .expectStatus().isFound();

        assertThatThrownBy(() -> articleRepository.findBy(lastSavedId))
                .isInstanceOf(CouldNotFindArticleIdException.class);
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteBy(savedArticleIdBeforeTest);
    }
}
