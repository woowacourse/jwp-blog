package techcourse.myblog.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.service.dto.ArticleRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @TestPropertySource("classpath:application_test.properties")
public class ArticleControllerTests {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerTests.class);

    private static final long EMPTY_ID = -1;
    private static final ArticleRequestDto ARTICLE_REQUEST_DTO = new ArticleRequestDto(EMPTY_ID, "testTitle", "testCoverUrl", "testContents");

    private static String testTitle = "testTitle";
    private static String testCoverUrl = "testCoverUrl";
    private static String testContents = "testContents";
    private static String testUniContents = StringEscapeUtils.escapeJava(testContents);

    private WebTestClient webTestClient;
    private String cookie;

    @Autowired
    public ArticleControllerTests(WebTestClient webTestClient) {
        log.info("creator called");

        this.webTestClient = webTestClient;
        this.cookie = LogInTestHelper.makeLoggedInCookie(webTestClient);
    }

    @Test
    void showArticleWritingPageTest() {
        ResponseSpec rs = webTestClient.get()
                .uri("/writing")
                .header("Cookie", cookie)
                .exchange();

        rs.expectStatus()
                .isOk();
    }

    @Test
    void addNewArticleTest() {
        testSaveNewArticle(ARTICLE_REQUEST_DTO);
    }

    @Test
    void showArticlesPageTest() {
        ResponseSpec rs = webTestClient.get()
                .uri("/")
                .exchange();

        rs.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(innerResponse -> {
                    String body = new String(innerResponse.getResponseBody());
                    assertThat(body.contains(testTitle)).isTrue();
                    assertThat(body.contains(testCoverUrl)).isTrue();
                    assertThat(body.contains(testContents)).isTrue();
                });
    }

    @Test
    void showArticleByIdPageTest() {
        ResponseSpec rs = webTestClient.get()
                .uri("/articles/1")
                .exchange();

        rs.expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(testTitle)).isTrue();
                    assertThat(body.contains(testCoverUrl)).isTrue();
                    assertThat(body.contains(testUniContents)).isTrue();
                });
    }

    @Test
    void updateArticleByIdPageTest() {
        String updatedTitle = "포비의 글";
        String updatedCoverUrl = "http://www.kinews.net/news/photo/200907/bjs.jpg";
        String updatedContents = "나는 우아한형제들에서 짱이다.";
        String updatedUniContents = StringEscapeUtils.escapeJava(updatedContents);

        ResponseSpec rs = webTestClient.put()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", updatedTitle)
                        .with("id", "1")
                        .with("coverUrl", updatedCoverUrl)
                        .with("contents", updatedContents))
                .exchange();

        rs.expectStatus()
                .is3xxRedirection();


        String redirectUrl = figureOutRedirectUrl(rs);
        webTestClient.get().uri(redirectUrl)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(innerResponse -> {
                    String body = new String(innerResponse.getResponseBody());
                    assertThat(body.contains(updatedTitle)).isTrue();
                    assertThat(body.contains(updatedCoverUrl)).isTrue();
                    assertThat(body.contains(updatedUniContents)).isTrue();
                });

        // 이것도 복구코드다...
        // TODO: 테스트와 디비내용 분리..!
        webTestClient.put()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", testTitle)
                        .with("id", "1")
                        .with("coverUrl", testCoverUrl)
                        .with("contents", testContents))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void deleteArticleByIdTest() {
        ResponseSpec rs = webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", testTitle)
                        .with("coverUrl", testCoverUrl)
                        .with("contents", testContents))
                .exchange();

        rs.expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String redirectUrl = response.getResponseHeaders().getLocation().toString();
                    webTestClient.delete().uri(redirectUrl)
                            .exchange()
                            .expectStatus()
                            .is3xxRedirection();
                });
    }

    @Test
    void 로그인_상태_수정페이지_접근_가능() {
        ResponseSpec rs = webTestClient.get()
                .uri("/articles/1/edit")
                .header("Cookie", cookie)
                .exchange();

        rs.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(testTitle)).isTrue();
                    assertThat(body.contains(testCoverUrl)).isTrue();
                    assertThat(body.contains(testUniContents)).isTrue();
                });
    }

    @Test
    void 로그인_안된_상태_수정페이지_접근_불가() {
        ResponseSpec rs = webTestClient.get()
                .uri("/articles/1/edit")
                .exchange();

        LogInTestHelper.assertLoginRedirect(rs);
    }

    @Test
    void 첫_페이지_방문_시_사용자_이름_GUEST_확인() {
        ResponseSpec rs = webTestClient.get()
                .uri("/")
                .exchange();

        rs.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("GUEST")).isTrue();
                    assertThat(body.contains("로그인")).isTrue();
                    assertThat(body.contains("회원가입")).isTrue();
                    assertThat(body.contains("로그아웃")).isFalse();
                });
    }


    @Test
    void 댓글_잘_나오는지_테스트() {
        ResponseSpec rs = webTestClient.get()
                .uri("/articles/1")
                .exchange();

        rs.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("first")).isTrue();
                    assertThat(body.contains("second")).isTrue();
                    assertThat(body.contains("third")).isTrue();
                });
    }

    @Test
    void 댓글_추가_테스트() {
        ResponseSpec rs = webTestClient.post()
                .uri("/articles/1/comments")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("comment", "good article"))
                .exchange()
                .expectStatus()
                .isFound();

        webTestClient.get()
                .uri("/articles/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("good article")).isTrue();
                });
    }

    @Test
    void 댓글_삭제_테스트() {
        ResponseSpec rs = webTestClient.get()
                .uri("/articles/1")
                .exchange();

        rs.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("FourthComment")).isTrue();
                });

        webTestClient.delete()
                .uri("/comments/4")
                .exchange()
                .expectStatus()
                .isFound();

        webTestClient.get()
                .uri("/articles/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("FourthComment")).isFalse();
                });
    }

    @Test
    void 댓글_수정_테스트() {
        webTestClient.get()
                .uri("/articles/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("FifthComment")).isTrue();
                });

        webTestClient.put()
                .uri("/comments/5")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("comment", "modifiedComment"))
                .exchange()
                .expectStatus()
                .isFound();

        webTestClient.get()
                .uri("/articles/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("FifthComment")).isFalse();
                    assertThat(body.contains("modifiedComment")).isTrue();
                });
    }

    private ResponseSpec addNewArticle(ArticleRequestDto dto) {
        return webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", dto.getTitle())
                        .with("coverUrl", dto.getCoverUrl())
                        .with("contents", dto.getContents()))
                .exchange();
    }

    private void testSaveNewArticle(ArticleRequestDto articleDto) {
        ResponseSpec rs = addNewArticle(articleDto);

        rs.expectStatus()
                .is3xxRedirection();

        String redirectUrl = figureOutRedirectUrl(rs);
        webTestClient.get().uri(redirectUrl)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(innerResponse -> {
                    String body = new String(innerResponse.getResponseBody());
                    assertThat(body.contains(articleDto.getTitle())).isTrue();
                    assertThat(body.contains(articleDto.getCoverUrl())).isTrue();
                    assertThat(body.contains(StringEscapeUtils.escapeJava(testContents))).isTrue();
                });
    }

    private String figureOutRedirectUrl(ResponseSpec rs) {
        String[] url = new String[]{""};
        rs.expectBody()
                .consumeWith(response -> {
                    url[0] = response
                            .getResponseHeaders()
                            .getLocation()
                            .toString();
                });

        return url[0];
    }
}
