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
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.utils.Utils;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    private static final String USER_NAME = "Ctest";
    private static final String EMAIL = "Ctest@test.com";
    private static final String PASSWORD = "CassWord!1";

    private static final String USER_NAME_2 = "CCCCtest";
    private static final String EMAIL_2 = "CCCCtest@test.com";
    private static final String PASSWORD_2 = "CCCCCCassWord!1";

    private static final String TITLE = "Ctitle";
    private static final String COVER_URL = "CcoverUrl";
    private static final String CONTENTS = "Ccontents blabla.";

    private static final String COMMENTS_CONTENTS = "Ccomment_contents";
    private static final String COMMENTS_CONTENTS_2 = "Ccomment_contents2";
    private static final String COMMENTS_CONTENTS_3 = "Ccomment_contents3";

    @LocalServerPort
    private int serverPort;

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;
    private String articleUrl;
    private String articleId;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + serverPort;

        Utils.createUser(webTestClient, new UserDto(USER_NAME, EMAIL, PASSWORD));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL, PASSWORD));

        ArticleDto articleDto = new ArticleDto(TITLE, COVER_URL, CONTENTS);

        articleUrl = Utils.createArticle(articleDto, cookie, baseUrl);
        articleId = Utils.getId(articleUrl);

        RequestCommentDto requestCommentDto = new RequestCommentDto(Long.parseLong(articleId), COMMENTS_CONTENTS);
        Utils.createComment(requestCommentDto, cookie, baseUrl);
        RequestCommentDto requestCommentDto2 = new RequestCommentDto(Long.parseLong(articleId), COMMENTS_CONTENTS_2);
        Utils.createComment(requestCommentDto2, cookie, baseUrl);
    }

    @Test
    @DisplayName("comment를 저장한다.")
    void save() {
        webTestClient.post().uri("/comments")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Cookie", cookie)
                .body(fromFormData("articleId", articleId)
                        .with("contents", COMMENTS_CONTENTS_2))
                .exchange()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get().uri(location)
                            .header("Cookie", cookie)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = Utils.getResponseBody(redirectResponse.getResponseBody());
                                assertThat(body).contains(COMMENTS_CONTENTS_2);
                            });
                });
    }

    @Test
    @DisplayName("comment 수정 페이지로 이동한다.")
    void commentEditForm() {
        webTestClient.post().uri("/comments")
                .header("Cookie", cookie)
                .body(fromFormData("articleId", articleId)
                        .with("contents", COMMENTS_CONTENTS))
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
                                assertThat(body).contains(COMMENTS_CONTENTS);
                            });
                });
    }

    @Test
    @DisplayName("comment를 수정한다.")
    void updateComment() {
        webTestClient.put().uri("/comments/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Cookie", cookie)
                .body(fromFormData("articleId", articleId)
                        .with("contents", COMMENTS_CONTENTS_3))
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
                                assertThat(body).contains(COMMENTS_CONTENTS_3);
                            });
                });
    }

    @Test
    @DisplayName("comment를 삭제한다.")
    void deleteComment() {
        webTestClient.delete().uri("/comments/2")
                .header("Cookie", cookie)
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
                                assertThat(body).doesNotContain(COMMENTS_CONTENTS_2);
                            });
                });
    }

    @Test
    @DisplayName("댓글을 권한에 따라 수정하지 못한다.")
    void canNotUpdateComment() {
        Utils.createUser(webTestClient, new UserDto(USER_NAME_2, EMAIL_2, PASSWORD_2));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL_2, PASSWORD_2));

        EntityExchangeResult<byte[]> result = webTestClient.put().uri("/comments/1")
                .header("Cookie", cookie)
                .header("Cookie", cookie)
                .body(fromFormData("articleId", articleId)
                        .with("contents", COMMENTS_CONTENTS_3))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        assertThat(result.toString()).contains("댓글을 작성한 유저만 수정할 수 있습니다.");

        Utils.deleteUser(webTestClient, cookie);
    }

    @Test
    @DisplayName("댓글을 권한에 따라 삭제하지 못한다.")
    void canNotDeleteComment() {
        Utils.createUser(webTestClient, new UserDto(USER_NAME_2, EMAIL_2, PASSWORD_2));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL_2, PASSWORD_2));

        RequestCommentDto requestCommentDto3 = new RequestCommentDto(Long.parseLong(articleId), COMMENTS_CONTENTS_3);
        String createLocationUrl = Utils.createComment(requestCommentDto3, cookie, baseUrl);
        String id = Utils.getId(createLocationUrl);

        EntityExchangeResult<byte[]> result = webTestClient.delete().uri("/comments/" + id)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        assertThat(result.toString()).contains("댓글을 작성한 유저만 수정할 수 있습니다.");

        Utils.deleteUser(webTestClient, cookie);
    }

    @AfterEach
    void tearDown() {
        Utils.deleteArticle(webTestClient, articleUrl);
        Utils.deleteUser(webTestClient, cookie);
    }
}