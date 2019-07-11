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
public class ArticleControllerTests {
    private static final int ARTICLE_TEST_ID = 1;
    public static final int ARTICLE_DELETE_TEST_ID = 2;
    private Article article;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        article = new Article("test title", "test coverUrl", "test contents", ARTICLE_TEST_ID);

        articleRepository.save(article);
    }

    @Test
    @DisplayName("index 페이지를 되돌려준다.")
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("새로운 article-edit 페이지를 되돌려준다.")
    void articleForm() {
        webTestClient.get().uri("/articles/new")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("/article-edit 요청에 대해 edit 페이지를 되돌려준다.")
    void articleEdit() {
        webTestClient.get().uri("/articles/" + ARTICLE_TEST_ID + "/edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("게시글을 작성한 뒤 생성 버튼을 눌렀을 때 생성된 게시글을 보여준다.")
    void articlePost() {
        String title = "테스트 타이틀 ";
        String coverUrl = "http://url.test";
        String contents = "테스트 컨텐츠";

        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
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
    @DisplayName("게시글을 작성한 뒤 생성 버튼을 눌렀을 때 생성된 게시글을 보여준다.")
    void getArticleEditPage() {
        webTestClient.get()
                .uri("/articles/" + ARTICLE_TEST_ID + "/edit")
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
    void deleteArticle() {
        Article deleteArticle = new Article(
                "deleting title",
                "deleting coverUrl",
                "deleting contents",
                ARTICLE_DELETE_TEST_ID
        );
        articleRepository.save(deleteArticle);
        webTestClient.delete()
                .uri("/articles/" + ARTICLE_DELETE_TEST_ID)
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
        articleRepository.delete(ARTICLE_TEST_ID);
    }
}
