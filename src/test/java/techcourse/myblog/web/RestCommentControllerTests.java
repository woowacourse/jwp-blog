package techcourse.myblog.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import techcourse.myblog.service.dto.CommentResponseDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class RestCommentControllerTests extends AbstractControllerTests {
    @Test
    void 댓글들_GET_요청_테스트() throws IOException {
        EntityExchangeResult<byte[]> result = getRequest("/articles/1/comments");
        String json = new String(result.getResponseBody());
        ObjectMapper objectMapper = new ObjectMapper();
        CommentResponseDto[] responses = objectMapper.readValue(json, CommentResponseDto[].class);
        assertThat(responses.length).isEqualTo(5);
    }
}
