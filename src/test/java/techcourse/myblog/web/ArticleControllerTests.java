package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final String BASE_URL = "/";
    private static final String ARTICLE_URL = "articles/";
    private static final long TARGET_ARTICLE_ID = 1;

    private static Article article = new Article("제목", "유알엘", "컨텐츠");

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
        get_요청_결과(BASE_URL)
                .expectStatus()
                .isOk();
    }

    private WebTestClient.ResponseSpec get_요청_결과(String uri) {
        return webTestClient.get().uri(uri)
                .exchange();
    }

    @Test
    void 게시물_작성_페이지_이동_테스트() {
        get_요청_결과(BASE_URL + "writing")
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시물_작성_요청_후_리다이렉팅_테스트() {
        webTestClient.post().uri(BASE_URL + ARTICLE_URL)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 게시물_조회_테스트() {
        String title = "제목";
        String coverUrl = "유알엘";
        String contents = "컨텐츠";

        get_요청_결과(BASE_URL + ARTICLE_URL + TARGET_ARTICLE_ID)
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
    void 게시물_수정_페이지_이동_테스트() {
        get_요청_결과(BASE_URL + ARTICLE_URL + TARGET_ARTICLE_ID + "/edit")
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시물_수정_요청_테스트() {
        webTestClient.put().uri(BASE_URL + ARTICLE_URL + TARGET_ARTICLE_ID)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 게시물_삭제_요청_테스트() {
        webTestClient.delete().uri(BASE_URL + ARTICLE_URL + TARGET_ARTICLE_ID)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @AfterEach
    void tearDown() {
        articleRepository = null;
    }
}
