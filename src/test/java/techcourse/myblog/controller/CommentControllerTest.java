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
import reactor.core.publisher.Mono;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.service.CommentService;
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

    @Autowired
    private CommentService commentService;


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
    }

    @Test
    @DisplayName("comment를 저장한다.")
    void save() {
        RequestCommentDto requestCommentDto = new RequestCommentDto(Long.parseLong(articleId), COMMENTS_CONTENTS_3);
        webTestClient.post().uri("/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(requestCommentDto), RequestCommentDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo(COMMENTS_CONTENTS_3)
                .jsonPath("$.userName").isEqualTo(USER_NAME);
    }

    @Test
    @DisplayName("comment를 수정한다.")
    void updateComment() {
        RequestCommentDto requestCommentDto = new RequestCommentDto(Long.parseLong(articleId), COMMENTS_CONTENTS);
        Utils.createComment(requestCommentDto, cookie, webTestClient);

        RequestCommentDto updateRequestCommentDto = new RequestCommentDto(Long.parseLong(articleId), COMMENTS_CONTENTS_2);

        webTestClient.put().uri("/comments/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header("Cookie", cookie)
                .body(Mono.just(updateRequestCommentDto), RequestCommentDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.contents").isEqualTo(COMMENTS_CONTENTS_2)
                .jsonPath("$.userName").isEqualTo(USER_NAME);
    }

    @Test
    @DisplayName("comment를 삭제한다.")
    void deleteComment() {
        //todo
//        RequestCommentDto requestCommentDto = new RequestCommentDto(Long.parseLong(articleId), COMMENTS_CONTENTS);
//        Utils.createComment(requestCommentDto, cookie, webTestClient);
//
//        webTestClient.delete().uri("/comments/1")
//                .header("Cookie", cookie)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.contents").isEqualTo(COMMENTS_CONTENTS)
//                .jsonPath("$.id").isEqualTo(Long.parseLong(articleId))
//                .jsonPath("$.userName").isEqualTo(USER_NAME);
    }

    @Test
    @DisplayName("댓글을 권한에 따라 수정하지 못한다.")
    void canNotUpdateComment() {
        //todo
//        Utils.createUser(webTestClient, new UserDto(USER_NAME_2, EMAIL_2, PASSWORD_2));
//        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL_2, PASSWORD_2));
//
//        RequestCommentDto updateRequestCommentDto = new RequestCommentDto(Long.parseLong(articleId), COMMENTS_CONTENTS_2);
//
//        EntityExchangeResult<byte[]> result = webTestClient.put().uri("/comments/1")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8)
//                .header("Cookie", cookie)
//                .body(Mono.just(updateRequestCommentDto), RequestCommentDto.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody()
//                .returnResult();
//
//        assertThat(result.toString()).contains("댓글을 찾을수 없습니다.");

    }

    @Test
    @DisplayName("댓글을 권한에 따라 삭제하지 못한다.")
    void canNotDeleteComment() {
        //todo
//        Utils.createUser(webTestClient, new UserDto(USER_NAME_2, EMAIL_2, PASSWORD_2));
//        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL_2, PASSWORD_2));
//
//        EntityExchangeResult<byte[]> result = webTestClient.delete().uri("/comments/1")
//                .header("Cookie", cookie)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody()
//                .returnResult();
//
//        assertThat(result.toString()).contains("댓글을 찾을수 없습니다.");
//
//        Utils.deleteUser(webTestClient, cookie);
    }

    @AfterEach
    void tearDown() {
        commentService.deleteAll();
    }
}