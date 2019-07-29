package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.apache.commons.lang3.StringEscapeUtils;
import techcourse.myblog.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private WebTestClient webTestClient;
    private static String testTitle = "목적의식 있는 연습을 통한 효과적인 학습";
    private static String testCoverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
    private static String testContents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 \"선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?\"였다.";
    private static String defaultTitle = "목적의식 있는 연습을 통한 효과적인 학습2";
    private static String defaultCoverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo2.jpeg";
    private static String defaultContents = "2나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 \"선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?\"였다.";
    private static String testUniContents = StringEscapeUtils.escapeJava(testContents);
    private static String defaultUniContents = StringEscapeUtils.escapeJava(defaultContents);
    private String defaultName = "default";
    private String defaultPassword = "abcdEFGH123!@#";
    private String defaultEmail = "default@default.com";
    private LoginControllerTest loginControllerTest;
    @Autowired
    private UserRepository userRepository;
    String cookie;

    @Autowired
    public ArticleControllerTests(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
        this.loginControllerTest = new LoginControllerTest(this.webTestClient, this.userRepository);
    }

    @BeforeEach
    void setUp() {
//        saveTestArticle();
        cookie = loginControllerTest.getLoginCookie(defaultEmail, defaultPassword);
    }

    @Test
    void showArticleWritingPageTest() {
        webTestClient.get()
                .uri("/articles/writing").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void saveArticlePageTest() {
        webTestClient.post()
                .uri("/articles").header("Cookie", cookie)
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
                    assertThat(body.contains(defaultTitle)).isTrue();
                    assertThat(body.contains(defaultCoverUrl)).isTrue();
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
                    assertThat(body.contains(defaultTitle)).isTrue();
                    assertThat(body.contains(defaultCoverUrl)).isTrue();
                    assertThat(body.contains(defaultUniContents)).isTrue();
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
                .uri("/articles/1").header("Cookie", cookie)
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
                .uri("/articles/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", defaultTitle)
                        .with("coverUrl", defaultCoverUrl)
                        .with("contents", defaultContents))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void deleteArticleByIdTest() {
        webTestClient.post()
                .uri("/articles").header("Cookie", cookie)
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
                    webTestClient.delete().uri(redirectUrl)
                            .exchange()
                            .expectStatus()
                            .is3xxRedirection();
                });
    }

    @Test
    void showArticleEditingPage() {
        webTestClient.get()
                .uri("/articles/1/edit").header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(defaultTitle)).isTrue();
                    assertThat(body.contains(defaultCoverUrl)).isTrue();
                    assertThat(body.contains(defaultUniContents)).isTrue();
                });
    }

    private void saveTestArticle() {
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", testTitle)
                        .with("coverUrl", testCoverUrl)
                        .with("contents", testContents))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
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
