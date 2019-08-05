package techcourse.myblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.function.Consumer;

@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyblogApplicationTests {
    protected final String COMMENT_CONTENTS = "testContents";
    protected final long USER_ID = 1;
    protected final String USER_NAME = "default";
    protected final String USER_PASSWORD = "asdASD12!@";
    protected final String USER_EMAIL = "hi@name.com";
    protected final long ARTICLE_ID = 1;
    protected final String ARTICLE_TITLE = "title";
    protected final String ARTICLE_CONTENTS = "contents";
    protected final String ARTICLE_COVER_URL = "coverUrl";
    protected final String LOGIN_ERROR_MESSAGE = "아이디나 비밀번호가 잘못되었습니다.";
    protected final String TEST_ARTICLE_TITLE = "testTitle";
    protected final String TEST_ARTICLE_COVER_URL = "testCoverUrl";
    protected final String TEST_ARTICLE_CONTENTS = "testContents";
    protected final long TEST_ARTICLE_ID = 2;
    @Autowired
    private WebTestClient webTestClient;

    protected StatusAssertions getRequestExpectStatus(HttpMethod httpMethod, String uri) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .exchange()
                .expectStatus();
    }

    protected String getLoginCookie(String email, String password) { //로그인 함.
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }

    protected StatusAssertions getRequestWithCookieExpectStatus(HttpMethod httpMethod, String uri, String cookie) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .exchange()
                .expectStatus();
    }

    protected StatusAssertions getResponseSpecWithCookieWithBody(HttpMethod httpMethod, String uri, String cookie, MultiValueMap<String, String> bodyData) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body((BodyInserters.fromFormData(bodyData)))
                .exchange()
                .expectStatus();

    }

    protected WebTestClient.RequestHeadersSpec getResponseSpec(HttpMethod httpMethod, String uri, MultiValueMap<String, String> bodyData) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .body((BodyInserters.fromFormData(bodyData)));
    }

    protected void confirmResponseBody(WebTestClient.ResponseSpec responseSpec, Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer) {
        responseSpec.expectBody().consumeWith(entityExchangeResultConsumer);
    }

    protected MultiValueMap<String, String> getCustomUserDtoMap(String name, String email, String password, long userId) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", name);
        map.add("email", email);
        map.add("password", password);
        map.add("id", String.valueOf(userId));
        return map;
    }

    protected MultiValueMap<String, String> getCustomArticleDtoMap(String title, String coverUrl, String contents, long articleId) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", title);
        map.add("coverUrl", coverUrl);
        map.add("contents", contents);
        map.add("id", String.valueOf(articleId));
        return map;
    }

    protected MultiValueMap<String, String> getCustomCommentDtoMap(String contents, long articleId) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("contents", contents);
        map.add("articleId", String.valueOf(articleId));
        return map;
    }

    protected String getRedirectUrl(StatusAssertions statusAssertions) {
        return statusAssertions.isFound().expectBody()
                .returnResult()
                .getResponseHeaders()
                .getLocation()
                .getPath();
    }


}