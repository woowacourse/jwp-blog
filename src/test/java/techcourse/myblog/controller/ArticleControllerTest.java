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
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.utils.Utils;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.linesOf;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static final String USER_NAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "passWord!1";

    private static final String TITLE = "title";
    private static final String COVER_URL = "coverUrl";
    private static final String CONTENTS = "contents blabla.";

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

        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(TITLE);
        articleDto.setCoverUrl(COVER_URL);
        articleDto.setContents(CONTENTS);

        articleUrl = Utils.createArticle(articleDto, cookie, baseUrl);
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .header("Cookie",cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/articles/writing")
                .header("Cookie",cookie)
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

    }

    @Test
    @DisplayName("게시물을 삭제한다.")
    void deleteArticle() {

    }

    @AfterEach
    void tearDown() {
        Utils.deleteAll(webTestClient);
        Utils.deleteUser(webTestClient, cookie);
    }
}
