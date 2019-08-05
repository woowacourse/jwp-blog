package techcourse.myblog.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.web.AuthedWebTestClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class CommentApiTest extends AuthedWebTestClient {
    private long articleId;

    @BeforeEach
    void setUp() {
        String id = post("/articles")
                .body(params(Arrays.asList("title", "contents", "coverUrl"), "title", "contents", "coverUrl"))
                .exchange()
                .expectBody().returnResult().getResponseHeaders().getLocation().getPath().split("/")[2];
        articleId = Long.parseLong(id);
    }

    @Test
    long create() throws IOException {
        CommentDto.JSON commentJson = new CommentDto.JSON("comment");
        EntityExchangeResult<byte[]> result = postJson("/api/articles/" + articleId + "/comments")
                .body(Mono.just(commentJson), CommentDto.JSON.class)
                .exchange()
                .expectBody().returnResult();

        String body = new String(Objects.requireNonNull(result.getResponseBody()));
        List<Comment> comments = new ObjectMapper().readValue(body, List.class);
        assertThat(comments).isNotEmpty();
        return comments.get(0).getId();
    }

    @Test
    void update() throws IOException {
        CommentDto.JSON commentJson = new CommentDto.JSON("edited");
        EntityExchangeResult<byte[]> result = putJson("/api/articles/" + articleId + "/comments/" + create())
                .body(Mono.just(commentJson), CommentDto.JSON.class)
                .exchange()
                .expectBody().returnResult();

        String body = new String(Objects.requireNonNull(result.getResponseBody()));
        List<Comment> comments = new ObjectMapper().readValue(body, List.class);
        assertThat(comments.stream()
                .anyMatch(comment -> comment.getContents().equals("edited")))
                .isTrue();
    }

    @Test
    void delete() throws IOException {
        EntityExchangeResult<byte[]> result = delete("/api/articles/" + articleId + "/comments/" + create())
                .exchange()
                .expectBody().returnResult();
        String body = new String(Objects.requireNonNull(result.getResponseBody()));
        List<Comment> comments = new ObjectMapper().readValue(body, List.class);
        assertThat(comments.stream()
                .anyMatch(comment -> comment.getContents().equals("comment")))
                .isFalse();
    }
}