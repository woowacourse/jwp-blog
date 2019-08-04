package techcourse.myblog.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.utils.Utils;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static final String USER_NAME = "atest";
    private static final String EMAIL = "atest@test.com";
    private static final String PASSWORD = "apassWord!1";

    private static final String USER_NAME_2 = "aatest";
    private static final String EMAIL_2 = "aatest@test.com";
    private static final String PASSWORD_2 = "aapassWord!1";

    private static final String TITLE = "atitle";
    private static final String COVER_URL = "acoverUrl";
    private static final String CONTENTS = "acontents blabla.";

    private static final String TITLE_2 = "atitle2";
    private static final String COVER_URL_2 = "acoverUrl2";
    private static final String CONTENTS_2 = "acontents blabla.2";

    @LocalServerPort
    private int serverPort;

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;
    private String articleUrl;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + serverPort;

        Utils.createUser(webTestClient, new UserDto(USER_NAME, EMAIL, PASSWORD));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL, PASSWORD));

        ArticleDto articleDto = new ArticleDto(TITLE, COVER_URL, CONTENTS);
        articleUrl = Utils.createArticle(articleDto, cookie, baseUrl);
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/articles/writing")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("새로운 article이 생성된다.")
    void createTest() {
        webTestClient.post().uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", TITLE)
                        .with("coverUrl", COVER_URL)
                        .with("contents", CONTENTS))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get().uri(location)
                            .header("Cookie", cookie)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = Utils.getResponseBody(redirectResponse.getResponseBody());
                                assertThat(body).contains(TITLE);
                                assertThat(body).contains(COVER_URL);
                                assertThat(body).contains(StringEscapeUtils.escapeJava(CONTENTS));
                            });
                });
    }

    @Test
    @DisplayName("게시물을 불러온다.")
    void findArticle() {
        webTestClient.get().uri(articleUrl)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = Utils.getResponseBody(response.getResponseBody());
                    assertThat(body).contains(TITLE);
                    assertThat(body).contains(COVER_URL);
                    assertThat(body).contains(StringEscapeUtils.escapeJava(CONTENTS));
                });
    }

    @Test
    @DisplayName("게시물을 수정한다.")
    void updateArticle() {
        webTestClient.put().uri(articleUrl)
                .header("Cookie", cookie)
                .body(fromFormData("title", TITLE_2)
                        .with("coverUrl", COVER_URL_2)
                        .with("contents", CONTENTS_2))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get().uri(location)
                            .header("Cookie", cookie)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = Utils.getResponseBody(redirectResponse.getResponseBody());
                                assertThat(body).contains(TITLE_2);
                                assertThat(body).contains(COVER_URL_2);
                                assertThat(body).contains(CONTENTS_2);
                            });
                });
    }

    @Test
    @DisplayName("게시물을 삭제한다.")
    void deleteArticle() {
        webTestClient.delete().uri(articleUrl)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    @DisplayName("게시물의 권한에 따라 수정하지 못한다.")
    void canNotUpdate() {
        Utils.createUser(webTestClient, new UserDto(USER_NAME_2, EMAIL_2, PASSWORD_2));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL_2, PASSWORD_2));

        EntityExchangeResult<byte[]> result = webTestClient.put().uri(articleUrl)
                .header("Cookie", cookie)
                .body(fromFormData("title", TITLE_2)
                        .with("coverUrl", COVER_URL_2)
                        .with("contents", CONTENTS_2))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        assertThat(result.toString()).contains("게시글을 작성한 유저만 수정할 수 있습니다.");

        Utils.deleteUser(webTestClient, cookie);
    }

    @Test
    @DisplayName("게시물의 권한에 따라 수정하지 못한다.")
    void canNotDelete() {
        Utils.createUser(webTestClient, new UserDto(USER_NAME_2, EMAIL_2, PASSWORD_2));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL_2, PASSWORD_2));

        EntityExchangeResult<byte[]> result = webTestClient.delete().uri(articleUrl)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        assertThat(result.toString()).contains("게시글을 작성한 유저만 수정할 수 있습니다.");

        Utils.deleteUser(webTestClient, cookie);
    }

    @AfterEach
    void tearDown() {
        Utils.deleteArticle(webTestClient, articleUrl);
        Utils.deleteUser(webTestClient, cookie);
    }
}
