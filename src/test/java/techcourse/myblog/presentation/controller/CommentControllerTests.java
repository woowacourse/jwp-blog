package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.dto.UserResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests extends ControllerTests {
    private static final Logger log = LoggerFactory.getLogger(CommentControllerTests.class);

    private static final String TITLE = "ThisIsTitle";
    private static final String COVER_URL = "ThisIsCoverUrl";
    private static final String CONTENTS = "ThisIsContents";
    private static final String EMAIL = "hard@gmail.com";
    private static final String NAME = "hard";
    private static final String PASSWORD = "qwerasdf";
    private static final String COMMENT_CONTENTS = "444";
    private static final String COMMENT_MODIFIED_CONTENTS = "445";

    @Autowired
    WebTestClient webTestClient;

    public CommentControllerTests() {
        log.info("[USER] - " + getUserId());
        log.info("[ARTICLE] - " + getArticleId());
        log.info("[COMMENT] - " + getCommentId());
    }

    @BeforeEach
    void setUp() {
        registerUser(NAME, EMAIL, PASSWORD);
    }

    @Test
    void create_댓글_작성_테스트() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);

        webTestClient.post().uri("/articles/" + articleId + "/comments")
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(gson.toJson(new CommentDto(null, COMMENT_CONTENTS, null))))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            CommentDto commentDto = gson.fromJson(new String(res.getResponseBody()), CommentDto.class);
            CommentDto expected = new CommentDto(countComment(), COMMENT_CONTENTS, new UserResponseDto(getUserId(), EMAIL, NAME));
            assertThat(commentDto.getAuthor().getId()).isEqualTo(expected.getAuthor().getId());
            assertThat(commentDto.getContents()).isEqualTo(expected.getContents());
            assertThat(commentDto.getId()).isEqualTo(expected.getId());
        });

        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isTrue();
            assertThat(body.contains(COMMENT_MODIFIED_CONTENTS)).isFalse();
        });
    }

    @Test
    void create_비로그인시_생성_에러_테스트() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);

        webTestClient.post().uri("/articles/" + articleId + "/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(gson.toJson(new CommentDto(null, COMMENT_CONTENTS, null))))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(body -> assertThat(body.getResponseHeaders().getLocation().getPath()).isEqualTo("/login"));

        webTestClient.get().uri("/articles/" + articleId)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isFalse();
        });
    }

    @Test
    void update_댓글_수정_테스트() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        Long commentId = createComments("444", articleId, sessionId);

        webTestClient.put().uri("/articles/" + articleId + "/comments/" + commentId)
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(gson.toJson(new CommentDto(null, COMMENT_MODIFIED_CONTENTS, null))))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(res -> {
            CommentDto commentDto = gson.fromJson(new String(res.getResponseBody()), CommentDto.class);
            assertThat(commentDto.getAuthor().getId()).isEqualTo(getUserId());
            assertThat(commentDto.getContents()).isEqualTo(COMMENT_MODIFIED_CONTENTS);
            assertThat(commentDto.getId()).isEqualTo(getCommentId());
        });

        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isFalse();
            assertThat(body.contains(COMMENT_MODIFIED_CONTENTS)).isTrue();
        });
    }

    @Test
    void update_작성자가_아닐때_에러_테스트() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        Long commentId = createComments(COMMENT_CONTENTS, articleId, sessionId);
        registerUser(NAME + "a", EMAIL + "a", PASSWORD + "a");
        sessionId = logInAndGetSessionId(EMAIL + "a", PASSWORD + "a");

        webTestClient.put().uri("/articles/" + articleId + "/comments/" + commentId)
                .header("Cookie", sessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(gson.toJson(new CommentDto(null, COMMENT_MODIFIED_CONTENTS, null))))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(body -> assertThat(body.getResponseHeaders().getLocation().getPath()).isEqualTo("/articles/" + articleId));

        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isTrue();
            assertThat(body.contains(COMMENT_MODIFIED_CONTENTS)).isFalse();
        });

        deleteUser(sessionId);
    }

    @Test
    void update_비로그인시_에러_테스트() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        Long commentId = createComments(COMMENT_CONTENTS, articleId, sessionId);

        webTestClient.put().uri("/articles/" + articleId + "/comments/" + commentId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(gson.toJson(new CommentDto(null, COMMENT_MODIFIED_CONTENTS, null))))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(body -> assertThat(body.getResponseHeaders().getLocation().getPath()).isEqualTo("/login"));

        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isTrue();
            assertThat(body.contains(COMMENT_MODIFIED_CONTENTS)).isFalse();
        });
    }

    @Test
    void delete_삭제_테스트() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        Long commentId = createComments(COMMENT_CONTENTS, articleId, sessionId);

        webTestClient.delete().uri("/articles/" + articleId + "/comments/" + commentId)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isFalse();
            assertThat(body.contains(COMMENT_MODIFIED_CONTENTS)).isFalse();
        });
    }

    @Test
    void delete_작성자가_아닐때_에러_테스트() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        Long commentId = createComments(COMMENT_CONTENTS, articleId, sessionId);
        registerUser(NAME + "a", EMAIL + "a", PASSWORD + "a");
        sessionId = logInAndGetSessionId(EMAIL + "a", PASSWORD + "a");

        webTestClient.delete().uri("/articles/" + articleId + "/comments/" + commentId)
                .header("Cookie", sessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(body -> assertThat(body.getResponseHeaders().getLocation().getPath()).isEqualTo("/articles/" + articleId));

        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isTrue();
            assertThat(body.contains(COMMENT_MODIFIED_CONTENTS)).isFalse();
        });

        deleteUser(sessionId);
    }

    @Test
    void delete_비로그인시_삭제_에러_테스트() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        Long articleId = createArticle(TITLE, COVER_URL, CONTENTS, sessionId);
        Long commentId = createComments(COMMENT_CONTENTS, articleId, sessionId);

        webTestClient.delete().uri("/articles/" + articleId + "/comments/" + commentId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().consumeWith(body -> assertThat(body.getResponseHeaders().getLocation().getPath()).isEqualTo("/login"));

        webTestClient.get().uri("/articles/" + articleId)
                .header("Cookie", sessionId)
                .exchange()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(COMMENT_CONTENTS)).isTrue();
            assertThat(body.contains(COMMENT_MODIFIED_CONTENTS)).isFalse();
        });
    }

    @AfterEach
    void tearDown() {
        String sessionId = logInAndGetSessionId(EMAIL, PASSWORD);
        deleteUser(sessionId);
    }
}