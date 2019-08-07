package techcourse.myblog.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;

import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class RestCommentControllerTests extends AbstractControllerTests {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 댓글들_GET_요청_테스트() throws IOException {
        String json = new String(Objects.requireNonNull(getRequest("/articles/1/comments").getResponseBody()));
        CommentResponseDto[] responses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(responses.length).isGreaterThanOrEqualTo(3);
    }

    @Test
    void 댓글_추가_POST_요청_테스트() throws IOException {
        String newComment = "new comment";
        String json = new String(Objects.requireNonNull(postJsonRequest("/articles/1/comments/rest", CommentRequestDto.class, newComment).getResponseBody()));
        CommentResponseDto[] responses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(responses[responses.length - 1].getComment()).isEqualTo(newComment);
    }

    @Test
    void 댓글_수정_PUT_요청_테스트() throws IOException {
        String updatedComment = "updated comment";
        String json = new String(Objects.requireNonNull(putJsonRequest("/comments/1/rest", CommentRequestDto.class, updatedComment).getResponseBody()));
        CommentResponseDto[] responses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(responses[0].getComment()).isEqualTo(updatedComment);
    }

    @Test
    void 댓글_삭제_DELETE_요청_테스트() throws IOException {
        String json = new String(Objects.requireNonNull(getRequest("/articles/1/comments").getResponseBody()));
        CommentResponseDto[] getResponses = objectMapper.readValue(json, CommentResponseDto[].class);

        json = new String(Objects.requireNonNull(deleteRequest("/comments/3/rest").getResponseBody()));
        CommentResponseDto[] deleteResponses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(getResponses.length).isGreaterThan(deleteResponses.length);
    }

    @Test
    void 자신이_작성하지_않은_댓글_수정_실패_테스트() throws IOException {
        String updatedComment = "updated comment";
        loginRequest("moomin@naver.com", "mooMIN123!@#");
        assertThat(putJsonRequest("/comments/1/rest", CommentRequestDto.class, updatedComment).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 자신이_작성하지_않은_댓글_삭제_실패_테스트() throws IOException {
        loginRequest("moomin@naver.com", "mooMIN123!@#");
        assertThat(deleteRequest("/comments/1/rest").getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
