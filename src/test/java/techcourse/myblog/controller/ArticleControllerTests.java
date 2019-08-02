package techcourse.myblog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.MyblogApplicationTests;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.controller.ArticleController.ARTICLE_URL;

@AutoConfigureWebTestClient
public class ArticleControllerTests extends MyblogApplicationTests {
    String cookie;

    @BeforeEach
    void setUp() {
        cookie = getLoginCookie(USER_EMAIL, USER_PASSWORD);
    }

    @Test
    @DisplayName("wiritng페이지로 잘 가는 지 테스트")
    void showArticleWritingPageTest() {
        getRequestWithCookieExpectStatus(HttpMethod.GET, ARTICLE_URL + "/writing", cookie)
                .isOk();
    }

    @Test
    @DisplayName("아티클 저장 잘 되는 지 테스트")
    void saveArticlePageTest() {
        MultiValueMap<String, String> map = getCustomArticleDtoMap(TEST_ARTICLE_TITLE, TEST_ARTICLE_COVER_URL, TEST_ARTICLE_CONTENTS, TEST_ARTICLE_ID);
        StatusAssertions statusAssertions = getResponseSpecWithCookieWithBody(HttpMethod.POST, ARTICLE_URL, cookie, map);

        String redirecUrl = getRedirectUrl(statusAssertions);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(TEST_ARTICLE_TITLE)).isTrue();
            assertThat(body.contains(TEST_ARTICLE_COVER_URL)).isTrue();
            assertThat(body.contains(TEST_ARTICLE_CONTENTS)).isTrue();
        };
        System.out.println(">>>" + redirecUrl);

        getRequestWithCookieExpectStatus(HttpMethod.GET, redirecUrl, cookie)
                .isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("로그인/미로그인 아티클목록 페이지 접근")
    void showArticlesPageTest() {
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(ARTICLE_TITLE)).isTrue();
            assertThat(body.contains(ARTICLE_COVER_URL)).isTrue();
        };
        getRequestExpectStatus(HttpMethod.GET, "/")
                .isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("로그인/미로그인 아티클 1번 페이지 접근")
    void showArticleByIdPageTest() {
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(ARTICLE_TITLE)).isTrue();
            assertThat(body.contains(ARTICLE_COVER_URL)).isTrue();
            assertThat(body.contains(ARTICLE_CONTENTS)).isTrue();
        };
        getRequestExpectStatus(HttpMethod.GET, ARTICLE_URL + "/1")
                .isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("아티클 삭제 테스트")
    void deleteArticleByIdTest() {
        getRequestWithCookieExpectStatus(HttpMethod.DELETE, ARTICLE_URL + "/1", cookie)
                .isFound();
    }

    @Test
    void showArticleEditingPage() {
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(ARTICLE_TITLE)).isTrue();
            assertThat(body.contains(ARTICLE_COVER_URL)).isTrue();
            assertThat(body.contains(ARTICLE_CONTENTS)).isTrue();
        };

        getRequestWithCookieExpectStatus(HttpMethod.GET, ARTICLE_URL + "/1/edit", cookie)
                .isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }


    @Test
    @DisplayName("로그인 후 수정 후 수정 잘됬는지 확인")
    void updateArticleByIdPageTest() {
        MultiValueMap<String, String> map = getCustomArticleDtoMap("updatedTitle", "updatedCoverUrl", "updatedContents", 1);
        StatusAssertions statusAssertions = getResponseSpecWithCookieWithBody(HttpMethod.PUT, ARTICLE_URL + "/1", cookie, map);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains("updatedTitle")).isTrue();
            assertThat(body.contains("updatedCoverUrl")).isTrue();
            assertThat(body.contains("updatedContents")).isTrue();
        };
        String redirectUrl = getRedirectUrl(statusAssertions);
        getRequestWithCookieExpectStatus(HttpMethod.GET, redirectUrl, cookie)
                .isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);

    }

    @Test
    @DisplayName("미로그인 시 아티클 수정 버튼 미노출")
    void hide_updateButton_Article() {
        String deleteBtnId = "delete-btn";
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(deleteBtnId)).isFalse();
        };
        getRequestExpectStatus(HttpMethod.GET, ARTICLE_URL + "/1")
                .isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }
    @Test
    @DisplayName("아티클저자가 아닌 유저가 아티클 수정 페이지 접근시 리다이렉트")
    void another_user_do_not_access_article_edit() {
        String cookie = getLoginCookie("kangmin789@naver.com","asdASD12!@");

        getRequestWithCookieExpectStatus(HttpMethod.GET,ARTICLE_URL + "/1/edit",cookie)
                .isFound();
    }

    @Test
    @DisplayName("아티클저자가 아닌 유저가 아티클 put 접근시 리다이렉트")
    void another_user_do_not_access_article_put() {
        String cookie = getLoginCookie("default2@default.com" ,  "abcdEFGH123!@#");
        MultiValueMap<String, String > map = getCustomArticleDtoMap(ARTICLE_TITLE,ARTICLE_COVER_URL,ARTICLE_CONTENTS,ARTICLE_ID);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            assertThat(response.getResponseHeaders().getLocation().getPath().contains("/")).isTrue();
        };
        getResponseSpecWithCookieWithBody(HttpMethod.PUT,ARTICLE_URL + "/1",cookie,map)
                .isFound()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("아티클저자가 아닌 유저가 아티클 delete 접근시 리다이렉트")
    void another_user_do_not_access_article_delete() {
        String cookie = getLoginCookie("default2@default.com" ,  "abcdEFGH123!@#");
        MultiValueMap<String, String > map = getCustomArticleDtoMap(ARTICLE_TITLE,ARTICLE_COVER_URL,ARTICLE_CONTENTS,ARTICLE_ID);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            assertThat(response.getResponseHeaders().getLocation().getPath().contains(ARTICLE_URL+"/1")).isTrue();
        };
        getRequestWithCookieExpectStatus(HttpMethod.DELETE,ARTICLE_URL+"/1",cookie)
                .isFound()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }
}
