package techcourse.myblog.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.web.controller.BaseControllerTests;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentRestControllerTest extends BaseControllerTests {
    private static final Long ARTICLE_ID = 1L;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    private String jSessionId;
    private Long commentId;
    private URI baseUri = linkTo(CommentRestController.class, ARTICLE_ID).toUri();

    @BeforeEach
    void setUp() throws IOException {
        // 회원가입 & 로그인
        final String userPassword = "P@ssw0rd";
        final String userEmail = "emailArticle@gamil.com";
        addUser("name", userEmail, userPassword);
        jSessionId = getJSessionId(userEmail, userPassword);

        // 댓글 등록
        CommentDto.Create commentDto = new CommentDto.Create(ARTICLE_ID, "contents");
        EntityExchangeResult<byte[]> result = addComment(commentDto);

        // 댓글 id 추출
        String body = new String(Objects.requireNonNull(result.getResponseBody()));
        CommentDto.Response commentResponse = new ObjectMapper().readValue(body, CommentDto.Response.class);
        commentId = commentResponse.getId();
    }

    @Test
    void list() throws IOException {
        // when
        EntityExchangeResult<byte[]> result = webTestClient.get().uri("/api/articles/{articleId}/comments", ARTICLE_ID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody().returnResult();

        String body = new String(Objects.requireNonNull(result.getResponseBody()));
        List<CommentDto.Response> comments = new ObjectMapper().readValue(body, List.class);

        // then
        assertThat(comments).isNotEmpty();
    }

    @Test
    void delete() {
        webTestClient.delete().uri("/api/articles/1/comments/{id}", commentId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isNoContent()
                .expectHeader().valueMatches("Location", ".*" + baseUri.toString());
    }

    @Test
    void update() throws IOException {
        // given
        String contents = "changed contents";
        CommentDto.Update commentDto = new CommentDto.Update(commentId, ARTICLE_ID, contents);

        // when
        EntityExchangeResult<byte[]> result = webTestClient.put().uri("/api/articles/{articleId}/comments/{id}", ARTICLE_ID, commentId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .body(Mono.just(commentDto), CommentDto.Update.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody().returnResult();

        String body = new String(Objects.requireNonNull(result.getResponseBody()));
        CommentDto.Response expected = new ObjectMapper().readValue(body, CommentDto.Response.class);

        // then
        assertThat(commentDto.getContents()).isEqualTo(expected.getContents());
        assertThat(commentDto.getId()).isEqualTo(expected.getId());
    }

    private EntityExchangeResult<byte[]> addComment(final CommentDto.Create commentDto) {
        return webTestClient.post().uri("/api/articles/{articleId}/comments", ARTICLE_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .body(Mono.just(commentDto), CommentDto.Create.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody().returnResult();
    }
}