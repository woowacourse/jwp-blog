package techcourse.myblog.web;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.apache.commons.lang3.StringEscapeUtils;
import techcourse.myblog.domain.ArticleVO;
import techcourse.myblog.domain.CommentVO;
import techcourse.myblog.domain.UserVO;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.UserControllerTest.testEmail;
import static techcourse.myblog.web.UserControllerTest.testPassword;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private WebTestClient webTestClient;
    private static String testTitle = "testTitle";
    private static String testCoverUrl = "testCoverUrl";
    private static String testContents = "testContents";
    private static String testUniContents = StringEscapeUtils.escapeJava(testContents);
    private String cookie;

    @Autowired
    public ArticleControllerTests(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @BeforeEach
    void setUp() {
        this.cookie = webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(UserVO.class, "", testPassword, testEmail))
                .exchange()
                .expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    @Test
    void showArticleWritingPageTest() {
        webTestClient.get()
                .uri("/articles/writing")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void addNewArticleTest() {
        testSaveNewArticle("testArticle", "testCoverUrl", "testContents");
    }

    @Test
    void showArticlesPageTest() {
        webTestClient.get().uri("/articles")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(innerResponse -> {
                    String body = new String(innerResponse.getResponseBody());
                    assertThat(body.contains(testTitle)).isTrue();
                    assertThat(body.contains(testCoverUrl)).isTrue();
                    //assertThat(body.contains(testContents)).isTrue();
                });
    }

    @Test
    void showArticleByIdPageTest() {
        webTestClient.get().uri("/articles/1")
                .exchange()
                .expectStatus().isOk()
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

        webTestClient.put()
                .uri("/articles/1")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(ArticleVO.class, updatedTitle, updatedContents, updatedCoverUrl))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String redirectUrl = response.getResponseHeaders().getLocation().toString();
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
                });

        webTestClient.put()
                .uri("/articles/1")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(ArticleVO.class, testTitle, testContents, testCoverUrl))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void deleteArticleByIdTest() {
        webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(ArticleVO.class, testTitle, testContents, testCoverUrl))
                .exchange()
                .expectStatus()
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
    void showArticleEditingPage() {
        webTestClient.get()
                .uri("/articles/1/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
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
    void 첫_페이지_방문_시_사용자_이름_GUEST_확인() {
        webTestClient.get()
                .uri("/articles")
                .exchange()
                .expectStatus()
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
        webTestClient.get()
                .uri("/articles/1")
                .exchange()
                .expectStatus()
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
        webTestClient.post()
                .uri("/articles/1/comments")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(CommentVO.class, "good article"))
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
        webTestClient.get()
                .uri("/articles/1")
                .exchange()
                .expectStatus()
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
                .body(mapBy(CommentVO.class, "modifiedComment"))
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

    private void testSaveNewArticle(String title, String coverUrl, String contents) {
        webTestClient.post()
                .uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(ArticleVO.class, title, contents, coverUrl))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String redirectUrl = response.getResponseHeaders().getLocation().toString();
                    webTestClient.get().uri(redirectUrl)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(innerResponse -> {
                                String body = new String(innerResponse.getResponseBody());
                                assertThat(body.contains(title)).isTrue();
                                assertThat(body.contains(coverUrl)).isTrue();
                                assertThat(body.contains(StringEscapeUtils.escapeJava(testContents))).isTrue();
                            });
                });
    }

    private <T> BodyInserters.FormInserter<String> mapBy(Class<T> classType, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData(Strings.EMPTY, Strings.EMPTY);

        for (int i = 0; i < classType.getDeclaredFields().length; i++) {
            body.with(classType.getDeclaredFields()[i].getName(), parameters[i]);
        }
        return body;
    }
}
