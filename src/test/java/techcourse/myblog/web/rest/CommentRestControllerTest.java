package techcourse.myblog.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.web.LogInControllerTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.LogInControllerTest.USER_NAME;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    @DisplayName("댓글 생성 테스트")
    void createTest() throws IOException {
        CommentRequestDto requestDto = new CommentRequestDto(1L, "hello");

        EntityExchangeResult<byte[]> jsessionid = saveComment(requestDto);

        String body = new String(jsessionid.getResponseBody());
        CommentResponseDto comment = new ObjectMapper().readValue(body, CommentResponseDto.class);

        assertThat(comment.getId()).isNotNull();
        assertThat(comment.getComment()).isEqualTo("hello");
        assertThat(comment.getAuthorName()).isEqualTo(USER_NAME);
    }

    private EntityExchangeResult<byte[]> saveComment(CommentRequestDto requestDto) {
        return webTestClient.post().uri("/comment")
                .cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(requestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .returnResult();
    }

    @Test
    @DisplayName("댓글 변경 테스트")
    void updateTest() throws IOException {
        CommentRequestDto requestDto = new CommentRequestDto(1L, "hello");

        EntityExchangeResult<byte[]> resultBySave = saveComment(requestDto);
        String body = new String(resultBySave.getResponseBody());
        CommentResponseDto comment = new ObjectMapper().readValue(body, CommentResponseDto.class);

        Long articleId = requestDto.getArticleId();
        Long commentId = comment.getId();
        String uri = "/articles/" + articleId + "/comment/" + commentId;

        CommentRequestDto updateDto = new CommentRequestDto(1L, "update");

        EntityExchangeResult<byte[]> jsessionid = webTestClient.put().uri(uri)
                .cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(updateDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .returnResult();

        String resultByUpdate = new String(jsessionid.getResponseBody());
        CommentResponseDto updatedComment = new ObjectMapper().readValue(resultByUpdate, CommentResponseDto.class);

        assertThat(updatedComment.getId()).isNotNull();
        assertThat(updatedComment.getComment()).isEqualTo("update");
        assertThat(updatedComment.getAuthorName()).isEqualTo(USER_NAME);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteTest() throws IOException {
        CommentRequestDto requestDto = new CommentRequestDto(1L, "hello");

        EntityExchangeResult<byte[]> resultBySave = saveComment(requestDto);
        String body = new String(resultBySave.getResponseBody());
        CommentResponseDto comment = new ObjectMapper().readValue(body, CommentResponseDto.class);

        Long articleId = requestDto.getArticleId();
        Long commentId = comment.getId();
        String uri = "/articles/" + articleId + "/comment/" + commentId;

        EntityExchangeResult<byte[]> jsessionid = webTestClient.delete().uri(uri)
                .cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .returnResult();

        String resultByUpdate = new String(jsessionid.getResponseBody());
        CommentResponseDto deletedComment = new ObjectMapper().readValue(resultByUpdate, CommentResponseDto.class);

        assertThat(deletedComment.getId()).isEqualTo(commentId);
        assertThat(deletedComment.getAuthorId()).isNull();
    }
}
