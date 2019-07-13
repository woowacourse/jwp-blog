package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static Article article = Article.builder()
            .title("jaemok").coverUrl("yuarel").contents("naeyong").build();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository.save(article);
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_작성_페이지_이동_테스트() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_작성_요청_후_리다이렉팅_테스트() {
        webTestClient.post().uri("/articles")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 게시물_조회_테스트() {
        String title = "jaemok";
        String contents = "naeyong";

        webTestClient.get().uri("/articles/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    //assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                });
    }

    @Test
    void 게시물_추가_요청_테스트() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "제목")
                        .with("coverUrl", "유알엘")
                        .with("contents", "내용"))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 게시물_수정_페이지_이동_테스트() {
        webTestClient.get().uri("/articles/1/edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시물_수정_요청_테스트() {
        webTestClient.put().uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 게시물_삭제_요청_테스트() {
        webTestClient.delete().uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @AfterEach
    void tearDown() {
        articleRepository = null;
    }
}
