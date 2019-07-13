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

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        String title = "titleTest";
        String coverUrl = "coverUrlTest";
        String contents = "contentsTest";
        webTestClient.post()
                .uri("/write")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound()
        ;
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    void 게시글_페이지_정상_조회() {
        String title = "titleTest";
        String coverUrl = "coverUrlTest";
        String contents = "contentsTest";

        webTestClient.get()
                .uri("/articles/" + (articleRepository.nextId() - 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response2 -> {
                    String body = new String(response2.getResponseBody());
                    assertThat(body.contains(title)).isTrue();
                    assertThat(body.contains(coverUrl)).isTrue();
                    assertThat(body.contains(contents)).isTrue();
                })
        ;
    }

    @Test
    void 존재하지_않는_게시글_조회_에러() {
        webTestClient.get()
                .uri("/articles/" + ((articleRepository.nextId() - 1) + 1))
                .exchange()
                .expectStatus().is5xxServerError()
        ;
    }

    @Test
    void 게시글_수정페이지_이동() {
        webTestClient.get()
                .uri("/articles/" + (articleRepository.nextId() - 1) + "/edit")
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    void 게시글_수정() {
        String newTitle = "newTileTest";
        String newCoverUrl = "newCoverUrlTest";
        String newContents = "newContentsTest";

        webTestClient.put()
                .uri("/articles/" + (articleRepository.nextId() - 1))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", newTitle)
                        .with("coverUrl", newCoverUrl)
                        .with("contents", newContents))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response2 -> {
                    String body = new String(response2.getResponseBody());
                    assertThat(body.contains(newTitle)).isTrue();
                    assertThat(body.contains(newCoverUrl)).isTrue();
                    assertThat(body.contains(newContents)).isTrue();
                })
        ;
    }

    @AfterEach
    void 게시글_삭제() {
        webTestClient.delete()
                .uri("/articles/" + (articleRepository.nextId() - 1))
                .exchange()
                .expectStatus().isFound()
        ;
    }
}
