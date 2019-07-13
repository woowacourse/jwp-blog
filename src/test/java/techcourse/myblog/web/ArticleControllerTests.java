package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.apache.commons.lang3.StringEscapeUtils;
import techcourse.myblog.domain.Article;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private String title;
    private String coverUrl;
    private String contents;
    private String uniContents;

    @BeforeEach
    void setUp() {
        title = "목적의식 있는 연습을 통한 효과적인 학습";
        coverUrl = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo.jpeg";
        contents = "나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가였다.";
        uniContents = StringEscapeUtils.escapeJava(contents);
    }

    @Test
    void index() {
        checkGetStatus("/");
    }

    @Test
    void showArticleWritingPages() {
        checkGetStatus("/writing");
    }

    @Test
    void showArticles() {
        checkStatus(webTestClient.post(), "/articles");

        uniContents = contents;

        checkGetStatus("/")
                .expectBody()
                .consumeWith(this::checkBodyResponse);

        deleteMethod();
    }

    @Test
    void findArticleById() {
        checkStatus(webTestClient.post(), "/articles");

        checkGetStatus("/articles/1")
                .expectBody()
                .consumeWith(this::checkBodyResponse);

        deleteMethod();
    }

    @Test
    void saveArticle() {
        checkStatus(webTestClient.post(), "/articles")
                .expectBody()
                .consumeWith(this::checkBodyResponse);

        deleteMethod();
    }

    @Test
    void updateArticle() {
        title = "포비의 글";
        coverUrl = "http://www.kinews.net/news/photo/200907/bjs.jpg";
        contents = "나는 우아한형제들에서 짱이다.";
        uniContents = StringEscapeUtils.escapeJava(contents);

        checkStatus(webTestClient.post(), "/articles");

        checkStatus(webTestClient.put(), "/articles/1")
                .expectBody()
                .consumeWith(this::checkBodyResponse);

        deleteMethod();
    }

    @Test
    void deleteTest() {
        checkStatus(webTestClient.post(), "/articles");

        deleteMethod();

        webTestClient.get().uri("/articles/1")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @AfterEach
    void tearDown() {
        Article.initCurrentId();
    }

    private void checkBodyResponse(EntityExchangeResult<byte[]> response) {
        String body = new String(Objects.requireNonNull(response.getResponseBody()));
        assertThat(body.contains(title)).isTrue();
        assertThat(body.contains(coverUrl)).isTrue();
        assertThat(body.contains(uniContents)).isTrue();
    }

    private WebTestClient.ResponseSpec checkStatus(WebTestClient.RequestBodyUriSpec requestMethod, String uri) {
        return requestMethod
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isOk();
    }

    private void deleteMethod() {
        webTestClient.delete()
                .uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    private WebTestClient.ResponseSpec checkGetStatus(String uri) {
        return webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk();
    }
}
