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
import techcourse.myblog.web.view.CommentResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentRestControllerTest extends BaseControllerTests {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    private String jSessionId;
    private Long commentId;

    @BeforeEach
    void setUp() throws IOException {
        final String userPassword = "P@ssw0rd";
        final String userEmail = "emailArticle@gamil.com";

        addUser("name", userEmail, userPassword);
        jSessionId = getJSessionId(userEmail, userPassword);

        String contents = "changed contents";
        CommentDto.Create commentDto = new CommentDto.Create();
        commentDto.setContents(contents);

        EntityExchangeResult<byte[]> entityExchangeResult = webTestClient.post().uri("/api/articles/1/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .body(Mono.just(commentDto), CommentDto.Create.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody().returnResult();

        String body = new String(Objects.requireNonNull(entityExchangeResult.getResponseBody()));
        CommentResponse commentResponses = new ObjectMapper().readValue(body, CommentResponse.class);
        commentId = commentResponses.getId();
    }

    @Test
    void list() throws IOException {
        EntityExchangeResult<byte[]> entityExchangeResult = webTestClient.get().uri("/api/articles/1/comments")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody().returnResult();

        String body = new String(Objects.requireNonNull(entityExchangeResult.getResponseBody()));
        List<CommentDto.Response> comments = new ObjectMapper().readValue(body, List.class);
        assertThat(comments).isNotEmpty();
    }

    @Test
    void delete() throws IOException {
        webTestClient.delete().uri("/api/articles/1/comments/{id}", commentId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    void update() throws IOException {
        String contents = "changed contents";
        CommentDto.Create commentDto = new CommentDto.Create();
        commentDto.setContents(contents);

        webTestClient.put().uri("/api/articles/1/comments/{id}", commentId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .body(Mono.just(commentDto), CommentDto.Create.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }
}