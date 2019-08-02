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
import techcourse.myblog.utils.Utils;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static techcourse.myblog.utils.BlogBodyContentSpec.assertThatBodyOf;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest {
    private static final String USER_NAME = "atest";
    private static final String EMAIL = "atest@test.com";
    private static final String PASSWORD = "apassWord!1";

    private static final String TITLE = "atitle";
    private static final String COVER_URL = "acoverUrl";
    private static final String CONTENTS = "acontents blabla.";

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
    @DisplayName("index 페이지를 되돌려준다.")
    void index() {
        webTestClient.get().uri("/")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("article 작성 페이지를 되돌려준다.")
    void articleForm() {
        webTestClient.get().uri("/articles/writing")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("새로운 article이 생성된다.")
    void createTest() {
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/articles")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", TITLE)
                        .with("coverUrl", COVER_URL)
                        .with("contents", CONTENTS))
                .exchange()
                .expectStatus().is3xxRedirection();

        String redirectLocation = Utils.getRedirectedLocationOf(response);
        WebTestClient.ResponseSpec redirectedResponse = webTestClient.get().uri(redirectLocation)
                .header("Cookie", cookie)
                .exchange();

        assertThatBodyOf(redirectedResponse).contains(TITLE, COVER_URL, CONTENTS);
    }

    @Test
    @DisplayName("게시물을 불러온다.")
    void fetchArticle() {
        WebTestClient.ResponseSpec response = webTestClient.get().uri(articleUrl)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();

        assertThatBodyOf(response).contains(TITLE, COVER_URL, StringEscapeUtils.escapeJava(CONTENTS));
    }

    @Test
    @DisplayName("게시물을 수정한다.")
    void updateArticle() {
        String updatedTitle = "atitle2";
        String updatedCoverUrl = "acoverUrl2";
        String updatedContents = "acontents blabla.2";

        WebTestClient.ResponseSpec response = webTestClient.put().uri(articleUrl)
                .header("Cookie", cookie)
                .body(fromFormData("title", updatedTitle)
                        .with("coverUrl", updatedCoverUrl)
                        .with("contents", updatedContents))
                .exchange()
                .expectStatus().is3xxRedirection();

        String redirectLocation = Utils.getRedirectedLocationOf(response);
        WebTestClient.ResponseSpec redirectedResponse = webTestClient.get().uri(redirectLocation)
                .header("Cookie", cookie)
                .exchange();

        assertThatBodyOf(redirectedResponse).contains(updatedTitle, updatedCoverUrl, updatedContents);
    }

    @Test
    @DisplayName("게시물을 삭제한다.")
    void deleteArticle() {
        webTestClient.delete().uri(articleUrl)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @AfterEach
    void tearDown() {
        Utils.deleteArticle(webTestClient, articleUrl);
        Utils.deleteUser(webTestClient, cookie);
    }
}
