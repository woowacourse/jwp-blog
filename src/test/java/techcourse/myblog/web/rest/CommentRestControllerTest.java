package techcourse.myblog.web.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.web.LogInControllerTest;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.LogInControllerTest.USER_NAME;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRestControllerTest {

    public static final long BASE_ARTICLE_ID = 1L;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("댓글 생성 테스트")
    void createTest() {
        CommentRequestDto requestDto = new CommentRequestDto(1L, "hello");
        CommentResponseDto comment = save(requestDto);

        assertThat(comment.getId()).isNotNull();
        assertThat(comment.getComment()).isEqualTo("hello");
        assertThat(comment.getAuthorName()).isEqualTo(USER_NAME);
    }

    private CommentResponseDto save(CommentRequestDto requestDto) {
        return sendRequestDto(HttpMethod.POST, requestDto, "/comments");
    }

    private CommentResponseDto sendRequestDto(HttpMethod method, CommentRequestDto requestDto, String uri) {
        return webTestClient.method(method).uri(uri)
                .cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
                .body(Mono.just(requestDto), CommentRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(CommentResponseDto.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    @DisplayName("댓글 변경 테스트")
    void updateTest() {
        CommentRequestDto requestDto = new CommentRequestDto(BASE_ARTICLE_ID, "hello");
        CommentResponseDto comment = save(requestDto);

        Long commentId = comment.getId();

        CommentRequestDto updateDto = new CommentRequestDto(BASE_ARTICLE_ID, "update");
        String uri = "/comments/" + commentId;
        CommentResponseDto updatedComment = update(updateDto, uri);

        assertThat(updatedComment.getId()).isNotNull();
        assertThat(updatedComment.getComment()).isEqualTo("update");
        assertThat(updatedComment.getAuthorName()).isEqualTo(USER_NAME);
    }

    private CommentResponseDto update(CommentRequestDto updateDto, String uri) {
        return sendRequestDto(HttpMethod.PUT, updateDto, uri);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteTest() {
        CommentRequestDto requestDto = new CommentRequestDto(BASE_ARTICLE_ID, "hello");
        CommentResponseDto comment = save(requestDto);

        Long commentId = comment.getId();
        String uri = "/comments/" + commentId;
        CommentResponseDto deletedComment = delete(new CommentRequestDto(null, null), uri);

        assertThat(deletedComment.getId()).isEqualTo(commentId);
        assertThat(deletedComment.getAuthorId()).isNull();
        assertThat(deletedComment.getAuthorName()).isNull();
        assertThat(deletedComment.getComment()).isNull();
    }

    private CommentResponseDto delete(CommentRequestDto commentRequestDto, String uri) {
        return sendRequestDto(HttpMethod.DELETE, commentRequestDto, uri);
    }
}
