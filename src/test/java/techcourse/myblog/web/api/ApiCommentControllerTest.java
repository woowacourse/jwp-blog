package techcourse.myblog.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.web.AuthedWebTestClient;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class ApiCommentControllerTest extends AuthedWebTestClient {

    @Test
    void create() throws IOException {
        CommentDto commentDto = new CommentDto("comment");
        EntityExchangeResult<byte[]> result = postJson("/api/articles/1/comments")
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().returnResult();

        String body = new String(Objects.requireNonNull(result.getResponseBody()));
        List<Comment> comments = new ObjectMapper().readValue(body, List.class);
        assertThat(comments).isNotEmpty();
    }

    @Test
    void updateAndDelete() throws IOException {
        CommentDto commentJson = new CommentDto("edited");
        putJson("/api/articles/1/comments/1")
                .body(Mono.just(commentJson), CommentDto.class)
                .exchange()
                .expectBody()
                .jsonPath("contents").isEqualTo("edited");


        delete("/api/articles/1/comments/1")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}