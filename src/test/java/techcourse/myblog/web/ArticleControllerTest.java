package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private String baseUrl;
    private String setUpArticleUrl;
    private ArticleDto setUpArticleDto;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + randomServerPort;
        setUpArticleDto = new ArticleDto("test title", "test coverUrl", "test contents");

        setUpArticleUrl = given()
                .param("title", setUpArticleDto.getTitle())
                .param("coverUrl", setUpArticleDto.getCoverUrl())
                .param("contents", setUpArticleDto.getContents())
                .post(baseUrl + "/write")
                .getHeader("Location");
    }

    @Test
    @DisplayName("index페이지에서 +버튼을 누르면 article-edit 페이지를 되돌려준다.")
    void articleCreationPageTest() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("게시글을 작성한 뒤 생성 버튼을 눌렀을 때 생성된 게시글을 보여준다.")
    void createNewArticleTest() {
        // Given
        ArticleDto newArticleDto = new ArticleDto(
                "new title",
                "new coverUrl",
                "new contents"
        );

        // When
        String createdArticleUrl = given()
                .param("title", newArticleDto.getTitle())
                .param("coverUrl", newArticleDto.getCoverUrl())
                .param("contents", newArticleDto.getContents())
                .post(baseUrl + "/write")
                .getHeader("Location");

        // Then
        webTestClient.get()
                .uri(createdArticleUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(
                            newArticleDto.getTitle(),
                            newArticleDto.getCoverUrl(),
                            newArticleDto.getContents()
                    );
                });
    }

    @Test
    @DisplayName("게시글에서 수정 버튼을 누르는 경우 edit 페이지에 해당 게시글을 넣어 되돌려준다.")
    void articleEditPageTest() {
        webTestClient.get()
                .uri(setUpArticleUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(
                            setUpArticleDto.getTitle(),
                            setUpArticleDto.getCoverUrl(),
                            setUpArticleDto.getContents()
                    );
                });
    }

    @Test
    @DisplayName("게시글 수정 후 저장 버튼을 누르면 수정된 게시글을 보여준다.")
    void articleEditTest() {
        // Givent
        ArticleDto updatedArticleDto = new ArticleDto(
                "changed title",
                "changed coverUrl",
                "changed contents"
        );

        // When
        String updatedArticleUrl = given()
                .param("title", updatedArticleDto.getTitle())
                .param("coverUrl", updatedArticleDto.getCoverUrl())
                .param("contents", updatedArticleDto.getContents())
                .put(setUpArticleUrl)
                .getHeader("Location");

        // Then
        webTestClient.get()
                .uri(updatedArticleUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(
                            updatedArticleDto.getTitle(),
                            updatedArticleDto.getCoverUrl(),
                            updatedArticleDto.getContents()
                    );
                });
    }

    @Test
    @DisplayName("게시글을 삭제한다.")
    void deleteArticleTest() {
        // Given
        ArticleDto articleDtoForDeleteTest = new ArticleDto(
                "deleting title",
                "deleting coverUrl",
                "deleting contents"
        );

        String testArticleUrl = given()
                .param("title", articleDtoForDeleteTest.getTitle())
                .param("coverUrl", articleDtoForDeleteTest.getCoverUrl())
                .param("contents", articleDtoForDeleteTest.getContents())
                .post(baseUrl + "/write")
                .getHeader("Location");

        // When
        String resultIndex = delete(testArticleUrl).getHeader("Location");

        // Then
        webTestClient.get()
                .uri(resultIndex)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).doesNotContain(
                            articleDtoForDeleteTest.getTitle(),
                            articleDtoForDeleteTest.getCoverUrl(),
                            articleDtoForDeleteTest.getContents()
                    );
                });
    }

    @AfterEach
    void tearDown() {
        delete(setUpArticleUrl);
    }
}
