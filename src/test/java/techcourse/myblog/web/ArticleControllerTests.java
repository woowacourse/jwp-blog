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
import org.springframework.test.web.reactive.server.EntityExchangeResult;
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
    private static String testCoverUrl = "https://www.incimages.com/uploaded_files/image/970x450/getty_509107562_2000133320009280346_351827.jpg";
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
        EntityExchangeResult<byte[]> result = getRequest("/articles/writing");
        assertThat(result.getStatus().is2xxSuccessful()).isTrue();
    }

    @Test
    void addNewArticleTest() {
        EntityExchangeResult<byte[]> postResponse = postFormRequest("/articles", ArticleVO.class, testTitle, testContents, testCoverUrl);
        EntityExchangeResult<byte[]> result = getRequest(postResponse.getResponseHeaders().getLocation().toString());
        String body = new String(result.getResponseBody());
        assertTest(body, testTitle, testContents, testCoverUrl);
    }

    @Test
    void showArticlesPageTest() {
        EntityExchangeResult<byte[]> result = getRequest("/articles");
        assertThat(result.getStatus().is2xxSuccessful()).isTrue();
        String body = new String(result.getResponseBody());
        assertTest(body, testTitle, testCoverUrl);
    }

    @Test
    void showArticleByIdPageTest() {
        EntityExchangeResult<byte[]> result = getRequest("/articles");
        assertThat(result.getStatus().is2xxSuccessful()).isTrue();
        String body = new String(result.getResponseBody());
        assertTest(body, testTitle, testCoverUrl, testUniContents);
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
                                assertTest(body, updatedTitle, updatedCoverUrl, updatedUniContents);
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
                    assertTest(body, testTitle, testCoverUrl, testUniContents);
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
                    assertTest(body, "GUEST", "로그인", "회원가입");
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
                    assertTest(body, "first", "second", "third");
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

    private <T> BodyInserters.FormInserter<String> mapBy(Class<T> classType, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData(Strings.EMPTY, Strings.EMPTY);

        for (int i = 0; i < classType.getDeclaredFields().length; i++) {
            body.with(classType.getDeclaredFields()[i].getName(), parameters[i]);
        }
        return body;
    }

    protected EntityExchangeResult<byte[]> getRequest(String uri) {
        return webTestClient.get()
                .uri(uri)
                .header("Cookie", cookie)
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected EntityExchangeResult<byte[]> postFormRequest(String uri, Class mappingClass, String... args) {
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(mappingClass, args))
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected EntityExchangeResult<byte[]> deleteRequest(String uri) {
        return webTestClient.delete()
                .uri(uri)
                .header("Cookie", cookie)
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected EntityExchangeResult<byte[]> putFormRequest(String uri, Class mappingClass, String... args) {
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(mappingClass, args))
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected void assertTest(String body, String... args) {
        for (String arg : args) {
            assertThat(body.contains(arg)).isTrue();
        }
    }
}
