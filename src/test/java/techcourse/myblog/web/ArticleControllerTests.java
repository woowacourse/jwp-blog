package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ArticleControllerTests {
    private static final String BASE_URL = "/";
    private static final String ARTICLE_URL = "articles/";
    private static long TARGET_ARTICLE_ID = 1;
    private static final String title = "제목";
    private static final String coverUrl = "유알엘";
    private static final String contents = "컨텐츠";
    private Article article;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        article = new Article(title, coverUrl, contents);
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
        articleRepository.save(article);
        Iterable<Article> all = articleRepository.findAll();
        System.out.println();
        get_요청_결과(BASE_URL + ARTICLE_URL + TARGET_ARTICLE_ID)
                .expectBody().consumeWith(res -> System.out.println(res.getResponseBody()));
    }

    @Test
    void 게시물_수정_페이지_이동_테스트() {
        articleRepository.save(article);
        get_요청_결과(BASE_URL + ARTICLE_URL + TARGET_ARTICLE_ID + "/edit")
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시물_수정_요청_테스트() {
        articleRepository.save(article);
        webTestClient.put().uri(BASE_URL + ARTICLE_URL + TARGET_ARTICLE_ID)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 게시물_삭제_요청_테스트() {
        articleRepository.save(article);
        webTestClient.delete().uri(BASE_URL + ARTICLE_URL + TARGET_ARTICLE_ID)
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}
