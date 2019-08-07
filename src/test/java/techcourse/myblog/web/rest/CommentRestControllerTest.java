package techcourse.myblog.web.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.web.LogInControllerTest;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.LogInControllerTest.USER_NAME;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("댓글 생성 테스트")
    void createTest() {
        CommentRequestDto requestDto = new CommentRequestDto(1L, "hello");

        EntityExchangeResult<CommentResponseDto> resultBySave = saveComment(requestDto);

        CommentResponseDto comment = resultBySave.getResponseBody();

        assertThat(comment.getId()).isNotNull();
        assertThat(comment.getComment()).isEqualTo("hello");
        assertThat(comment.getAuthorName()).isEqualTo(USER_NAME);
    }

    private EntityExchangeResult<CommentResponseDto> saveComment(CommentRequestDto requestDto) {
        String uri = "/comment";

        return getCommentApiResult(HttpMethod.POST, requestDto, uri);
    }

    @Test
    @DisplayName("댓글 변경 테스트")
    void updateTest() {
        CommentRequestDto requestDto = new CommentRequestDto(1L, "hello");

        EntityExchangeResult<CommentResponseDto> resultBySave = saveComment(requestDto);
        CommentResponseDto comment = resultBySave.getResponseBody();

        Long articleId = requestDto.getArticleId();
        Long commentId = comment.getId();
        String uri = "/articles/" + articleId + "/comment/" + commentId;

        CommentRequestDto updateDto = new CommentRequestDto(1L, "update");

        EntityExchangeResult<CommentResponseDto> result = getCommentApiResult(HttpMethod.PUT, updateDto, uri);

        CommentResponseDto updatedComment = result.getResponseBody();

        assertThat(updatedComment.getId()).isNotNull();
        assertThat(updatedComment.getComment()).isEqualTo("update");
        assertThat(updatedComment.getAuthorName()).isEqualTo(USER_NAME);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteTest() {
        CommentRequestDto requestDto = new CommentRequestDto(1L, "hello");

        EntityExchangeResult<CommentResponseDto> resultBySave = saveComment(requestDto);
        CommentResponseDto comment = resultBySave.getResponseBody();

        Long articleId = requestDto.getArticleId();
        Long commentId = comment.getId();
        String uri = "/articles/" + articleId + "/comment/" + commentId;


        EntityExchangeResult<CommentResponseDto> result = getCommentApiResult(HttpMethod.DELETE, new CommentRequestDto(null, null), uri);

        CommentResponseDto deletedComment = result.getResponseBody();

        assertThat(deletedComment.getId()).isEqualTo(commentId);
        assertThat(deletedComment.getAuthorId()).isNull();
    }

    private EntityExchangeResult<CommentResponseDto> getCommentApiResult(HttpMethod method, CommentRequestDto body, String uri) {
        return webTestClient.method(method).uri(uri)
                .cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(body), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(CommentResponseDto.class)
                .returnResult();
    }
}
