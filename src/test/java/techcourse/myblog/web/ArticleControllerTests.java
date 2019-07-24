package techcourse.myblog.web;

import org.apache.commons.lang3.StringEscapeUtils;
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
import techcourse.myblog.domain.ArticleRepository;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleRepository articleRepository;

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
        checkGetStatus("/articles/writing");
    }

    @Test
    void findArticleById() {
        EntityExchangeResult<byte[]> result = addArticle();

        checkGetStatus("/articles/" + getArticleId(result))
                .expectBody()
                .consumeWith(this::checkBodyResponse);
    }

    @Test
    void saveArticle() {
        checkStatus(webTestClient.post(), "/articles")
                .expectStatus().is3xxRedirection()
                .expectBody()
                .returnResult();
    }

    @Test
    void updateArticle() {
        title = "포비의 글";
        coverUrl = "http://www.kinews.net/news/photo/200907/bjs.jpg";
        contents = "나는 우아한형제들에서 짱이다.";
        uniContents = StringEscapeUtils.escapeJava(contents);
        EntityExchangeResult<byte[]> result = addArticle();

        int articleId = getArticleId(result);

        checkStatus(webTestClient.put(), "/articles/" + articleId)
                .expectBody()
                .consumeWith(this::checkBodyResponse)
                .returnResult();
    }

    @Test
    void deleteTest() {
        EntityExchangeResult<byte[]> result = addArticle();

        deleteMethod(getArticleId(result));

        webTestClient.get().uri("/articles/" + getArticleId(result))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    private EntityExchangeResult<byte[]> addArticle() {
        return checkStatus(webTestClient.post(), "/articles")
                .expectBody()
                .returnResult();
    }

    private WebTestClient.ResponseSpec checkStatus(WebTestClient.RequestBodyUriSpec requestMethod, String uri) {
        return requestMethod
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange();
    }

    private void deleteMethod(int id) {
        webTestClient.delete()
                .uri("/articles/" + id)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    private void checkBodyResponse(EntityExchangeResult<byte[]> response) {
        String body = new String(Objects.requireNonNull(response.getResponseBody()));
        assertThat(body.contains(title)).isTrue();
        assertThat(body.contains(coverUrl)).isTrue();
        assertThat(body.contains(uniContents)).isTrue();
    }

    private WebTestClient.ResponseSpec checkGetStatus(String uri) {
        return webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk();
    }

    private int getArticleId(EntityExchangeResult<byte[]> result) {
        return Integer.parseInt(result.getResponseHeaders().getLocation().getPath().split("/")[2]);
    }
}


