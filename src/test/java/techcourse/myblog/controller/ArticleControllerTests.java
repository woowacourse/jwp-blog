package techcourse.myblog.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.controller.ArticleController.ARTICLE_URL;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests extends MyblogApplicationTests {
    private WebTestClient webTestClient;
    private static String testTitle = "목적의식 있는 연습을 통한 효과적인 학습";
    private static String testCoverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
    private static String testContents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 \"선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?\"였다.";
    private static String testUniContents = StringEscapeUtils.escapeJava(testContents);
    private String default2Password = "abcdEFGH123!@#";
    private String default2Email = "default2@default.com";
    private LoginControllerTest loginControllerTest;

    @Autowired
    private UserRepository userRepository;
    String cookie;

    @Autowired
    public ArticleControllerTests(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
        this.loginControllerTest = new LoginControllerTest(this.webTestClient, userRepository);
    }

    @BeforeEach
    void setUp() {
        cookie = loginControllerTest.getLoginCookie(USER_EMAIL, USER_PASSWORD);
    }

    @Test
    void showArticleWritingPageTest() {
        webTestClient.get()
                .uri(ARTICLE_URL + "/writing").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void saveArticlePageTest() {
        webTestClient.post()
                .uri(ARTICLE_URL).header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", testTitle)
                        .with("coverUrl", testCoverUrl)
                        .with("contents", testContents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String redirectUrl = response.getResponseHeaders().getLocation().toString();
                    webTestClient.get().uri(redirectUrl)//.header("Cookie", cookie)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody()
                            .consumeWith(innerResponse -> {
                                String body = new String(innerResponse.getResponseBody());
                                assertThat(body.contains(testTitle)).isTrue();
                                assertThat(body.contains(testCoverUrl)).isTrue();
                                assertThat(body.contains(testUniContents)).isTrue();
                            });
                });
    }

    @Test
    void showArticlesPageTest() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(innerResponse -> {
                    String body = new String(innerResponse.getResponseBody());
                    assertThat(body.contains(ARTICLE_TITLE)).isTrue();
                    assertThat(body.contains(ARTICLE_COVER_URL)).isTrue();
                    //assertThat(body.contains(testContents)).isTrue();
                });
    }

    @Test
    void showArticleByIdPageTest() {
        webTestClient.get().uri(ARTICLE_URL + "/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(ARTICLE_TITLE)).isTrue();
                    assertThat(body.contains(ARTICLE_COVER_URL)).isTrue();
                    assertThat(body.contains(ARTICLE_UNI_CONTENTS)).isTrue();
                });
    }

    @Test
    void deleteArticleByIdTest() {
        String deleteTitle = "delete2";
        String deleteCoverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String deleteContents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 \"선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?\"였다.";
        webTestClient.post()
                .uri(ARTICLE_URL).header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", deleteTitle)
                        .with("coverUrl", deleteCoverUrl)
                        .with("contents", deleteContents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String redirectUrl = response.getResponseHeaders().getLocation().toString();
                    webTestClient.delete().uri(redirectUrl).header("Cookie", cookie)
                            .exchange()
                            .expectStatus()
                            .is3xxRedirection()
                            .expectBody()
                            .consumeWith(innerResponse -> {
                                webTestClient.get().uri("/").exchange().expectBody().consumeWith(getResponse -> {
                                    String body = new String(getResponse.getResponseBody());
                                    assertThat(body.contains(deleteTitle)).isFalse();
                                });
                            });
                });
    }

    @Test
    void showArticleEditingPage() {
        webTestClient.get()
                .uri(ARTICLE_URL + "/1/edit").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(ARTICLE_TITLE)).isTrue();
                    assertThat(body.contains(ARTICLE_COVER_URL)).isTrue();
                    assertThat(body.contains(ARTICLE_UNI_CONTENTS)).isTrue();
                });
    }

    @Test
    void updateArticleByIdPageTest() {
        String updateId = "1";
        String updatedTitle = "포비의 글";
        String updatedCoverUrl = "http://www.kinews.net/news/photo/200907/bjs.jpg";
        String updatedContents = "나는 우아한형제들에서 짱이다.";
        String updatedUniContents = StringEscapeUtils.escapeJava(updatedContents);

        webTestClient.put()
                .uri(ARTICLE_URL + "/1").header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", updatedTitle)
                        .with("id", updateId)
                        .with("coverUrl", updatedCoverUrl)
                        .with("contents", updatedContents))
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
                .uri(ARTICLE_URL + "/1").header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", ARTICLE_TITLE)
                        .with("id", String.valueOf(ARTICLE_ID))
                        .with("coverUrl", ARTICLE_COVER_URL)
                        .with("contents", ARTICLE_CONTENTS))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void 비로그인_ArticlePage_접근시_수정_삭제버튼_미노출() {
        String editUrl = ARTICLE_URL + "/1/edit";
        String deleteBtnId = "delete-btn";
        webTestClient.get()
                .uri(ARTICLE_URL + "/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(editUrl)).isFalse();
                    assertThat(body.contains(deleteBtnId)).isFalse();
                });
    }

    @Test
    void 본인이_아닌_ArticlePage_접근시_수정_삭제버튼_미노출() {
        String cookie = loginControllerTest.getLoginCookie(default2Email, default2Password);
        String editUrl = ARTICLE_URL + "/1/edit";
        String deleteBtnId = "delete-btn";
        webTestClient.get()
                .uri(ARTICLE_URL + "/1").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(editUrl)).isFalse();
                    assertThat(body.contains(deleteBtnId)).isFalse();
                });
    }

    @Test
    void 본인이_아닌_ArticleEditPage_접근시_홈으로_리다이렉트() {
        String cookie = loginControllerTest.getLoginCookie(default2Email, default2Password);

        webTestClient.get()
                .uri(ARTICLE_URL + "/1/edit").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    void 본인이_아닌_Article_put_접근시_홈으로_리다이렉트() {
        String cookie = loginControllerTest.getLoginCookie(default2Email, default2Password);
        String updateId = "1";
        String updatedTitle = "포비의 글";
        String updatedCoverUrl = "http://www.kinews.net/news/photo/200907/bjs.jpg";
        String updatedContents = "나는 우아한형제들에서 짱이다.";

        webTestClient.put()
                .uri(ARTICLE_URL + "/1").header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", updatedTitle)
                        .with("id", updateId)
                        .with("coverUrl", updatedCoverUrl)
                        .with("contents", updatedContents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("articles")).isFalse();
                });
    }

    @Test
    void 본인이_아닌_Article_delete_접근시_홈으로_리다이렉트() {
        String deleteCookie = loginControllerTest.getLoginCookie(default2Email, default2Password);
        String deleteTitle = "delete";
        String deleteCoverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        String deleteContents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 \"선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?\"였다.";
        webTestClient.post()
                .uri(ARTICLE_URL).header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", deleteTitle)
                        .with("coverUrl", deleteCoverUrl)
                        .with("contents", deleteContents))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String redirectUrl = response.getResponseHeaders().getLocation().toString();
                    webTestClient.delete().uri(redirectUrl).header("Cookie", deleteCookie)
                            .exchange()
                            .expectStatus()
                            .is3xxRedirection()
                            .expectBody()
                            .consumeWith(innerResponse -> {
                                webTestClient.get().uri("/").exchange().expectBody().consumeWith(getResponse -> {
                                    String body = new String(getResponse.getResponseBody());
                                    assertThat(body.contains(deleteTitle)).isTrue();
                                });
                            });
                });
    }

    @Test
    void 첫_페이지_방문_시_사용자_이름_GUEST_확인() {
        webTestClient.get()
                .uri("/")
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
}
