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
    private static final String NEW_COMMENT = "new comment";
    private static final String UPDATED_COMMENT = "updated comment";
    private static final String ANOTHER_USER_EMAIL = "moomin@naver.com";
    private static final String ANOTHER_USER_PASSWORD = "mooMIN123!@#";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 댓글들_GET_요청_테스트() throws IOException {
        String json = new String(Objects.requireNonNull(getRequest("/api/articles/1/comments").getResponseBody()));
        CommentResponseDto[] responses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(responses.length).isGreaterThanOrEqualTo(3);
    }

    @Test
    void 댓글_추가_POST_요청_테스트() throws IOException {
        String json = new String(Objects.requireNonNull(postJsonRequest("/api/articles/1/comments", CommentRequestDto.class, NEW_COMMENT).getResponseBody()));
        CommentResponseDto[] responses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(responses[responses.length - 1].getComment()).isEqualTo(NEW_COMMENT);
    }

    @Test
    void 댓글_수정_PUT_요청_테스트() throws IOException {
        String json = new String(Objects.requireNonNull(putJsonRequest("/api/comments/1", CommentRequestDto.class, UPDATED_COMMENT).getResponseBody()));
        CommentResponseDto[] responses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(responses[0].getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    void 댓글_삭제_DELETE_요청_테스트() throws IOException {
        String json = new String(Objects.requireNonNull(getRequest("/api/articles/1/comments").getResponseBody()));
        CommentResponseDto[] getResponses = objectMapper.readValue(json, CommentResponseDto[].class);

        json = new String(Objects.requireNonNull(deleteRequest("/api/comments/2").getResponseBody()));
        CommentResponseDto[] deleteResponses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(getResponses.length).isGreaterThan(deleteResponses.length);
    }

    @Test
    void 자신이_작성하지_않은_댓글_수정_실패_테스트() {
        loginRequest(ANOTHER_USER_EMAIL, ANOTHER_USER_PASSWORD);
        assertThat(putJsonRequest("/api/comments/1", CommentRequestDto.class, UPDATED_COMMENT).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 자신이_작성하지_않은_댓글_삭제_실패_테스트() {
        loginRequest(ANOTHER_USER_EMAIL, ANOTHER_USER_PASSWORD);
        assertThat(deleteRequest("/api/comments/1").getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
