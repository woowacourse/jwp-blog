package techcourse.myblog.controller;

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
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.utils.Utils;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static techcourse.myblog.utils.BlogBodyContentSpec.assertThatBodyOf;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    private static final String USER_NAME = "Ctest";
    private static final String EMAIL = "Ctest@test.com";
    private static final String PASSWORD = "CassWord!1";

    private static final String TITLE = "Ctitle";
    private static final String COVER_URL = "CcoverUrl";
    private static final String CONTENTS = "Ccontents blabla.";

    private static final String COMMENTS_CONTENTS = "Ccomment_contents";
    private static final String NEW_CONTENTS = "Ccomment_contents2";
    private static final String UPDATED_CONTENTS = "Ccomment_contents3";
    private static final String CONTENTS_FOR_DELETE_TEST = "Ccomment_contents4";

    @LocalServerPort
    private int serverPort;

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;
    private String baseUrl;
    private String articleUrl;
    private String articleId;
    private RequestCommentDto requestCommentDto;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + serverPort;

        Utils.createUser(webTestClient, new UserDto(USER_NAME, EMAIL, PASSWORD));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL, PASSWORD));

        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(TITLE);
        articleDto.setCoverUrl(COVER_URL);
        articleDto.setContents(CONTENTS);

        articleUrl = Utils.createArticle(articleDto, cookie, baseUrl);
        articleId = getArticleId(articleUrl);

        requestCommentDto = new RequestCommentDto();
        requestCommentDto.setArticleId(Long.parseLong(articleId));
    }

    private String getArticleId(String articleUrl) {
        List<String> list = Arrays.asList(articleUrl.split("/"));
        return list.get(list.size() - 1);
    }

    @Test
    @DisplayName("comment를 저장한다.")
    void save() {
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/comments")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Cookie", cookie)
                .body(fromFormData("articleId", articleId)
                        .with("contents", NEW_CONTENTS))
                .exchange()
                .expectStatus().is3xxRedirection();

        String redirectLocation = Utils.getRedirectedLocationOf(response);
        WebTestClient.ResponseSpec redirectedResponse = webTestClient.get().uri(redirectLocation)
                .header("Cookie", cookie)
                .exchange();

        assertThatBodyOf(redirectedResponse).contains(NEW_CONTENTS);
    }

    @Test
    @DisplayName("comment 수정 페이지로 이동한다.")
    void commentEditForm() {
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/comments")
                .header("Cookie", cookie)
                .body(fromFormData("articleId", articleId)
                        .with("contents", COMMENTS_CONTENTS))
                .exchange()
                .expectStatus().is3xxRedirection();

        String redirectLocation = Utils.getRedirectedLocationOf(response);
        WebTestClient.ResponseSpec redirectedResponse = webTestClient.get().uri(redirectLocation)
                .header("Cookie", cookie)
                .exchange();

        assertThatBodyOf(redirectedResponse).contains(COMMENTS_CONTENTS);
    }

    @Test
    @DisplayName("comment를 수정한다.")
    void updateComment() {
        requestCommentDto.setContents(COMMENTS_CONTENTS);
        Utils.createComment(requestCommentDto, cookie, baseUrl);

        WebTestClient.ResponseSpec response = webTestClient.put().uri("/comments/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Cookie", cookie)
                .body(fromFormData("articleId", articleId)
                        .with("contents", UPDATED_CONTENTS))
                .exchange()
                .expectStatus().is3xxRedirection();

        String redirectLocation = Utils.getRedirectedLocationOf(response);
        WebTestClient.ResponseSpec redirectedResponse = webTestClient.get().uri(redirectLocation)
                .header("Cookie", cookie)
                .exchange();

        assertThatBodyOf(redirectedResponse).contains(UPDATED_CONTENTS);
    }

    @Test
    @DisplayName("comment를 삭제한다.")
    void deleteComment() {
        requestCommentDto.setContents(CONTENTS_FOR_DELETE_TEST);
        Utils.createComment(requestCommentDto, cookie, baseUrl);

        WebTestClient.ResponseSpec response = webTestClient.delete().uri("/comments/1")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().is3xxRedirection();

        response.expectBody()
                .consumeWith(redirectedResponse -> {
                    URI location = redirectedResponse.getResponseHeaders().getLocation();
                    webTestClient.get().uri(location)
                            .header("Cookie", cookie)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = Utils.getResponseBody(redirectResponse.getResponseBody());
                                assertThat(body).doesNotContain(CONTENTS_FOR_DELETE_TEST);
                            });
                });
    }

    @AfterEach
    void tearDown() {
        Utils.deleteArticle(webTestClient, articleUrl);
        Utils.deleteUser(webTestClient, cookie);
    }
}