package techcourse.myblog.service.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.service.dto.comment.CommentRequestDto;
import techcourse.myblog.service.dto.comment.CommentResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CommentServiceTest {
    private static final Long DEFAULT_USER_ID = 999L;
    private static final Long DEFAULT_ARTICLE_ID = 999L;
    private static final Long DEFAULT_COMMENT_ID = 999L;


    @Autowired
    private CommentService commentService;

    @Test
    void 댓글_작성() {
        CommentRequestDto commentDto = new CommentRequestDto("comment");
        CommentResponseDto persistComment = commentService.save(commentDto, DEFAULT_USER_ID, DEFAULT_ARTICLE_ID);
        assertThat(persistComment.getContent()).isEqualTo(commentDto.getComment());
    }

    @Test
    void 댓글_수정() {
        CommentRequestDto commentDto = new CommentRequestDto("new Hello");
        CommentResponseDto updateComment = commentService.update(commentDto, DEFAULT_COMMENT_ID);
        assertThat(updateComment).isEqualTo(new CommentResponseDto(DEFAULT_COMMENT_ID, "new Hello", 1000L, "paul"));
    }

    @Test
    void 댓글_삭제() {
        commentService.delete(DEFAULT_COMMENT_ID);
        assertThatExceptionOfType(CommentNotFoundException.class)
                .isThrownBy(() -> commentService.findById(DEFAULT_COMMENT_ID));
    }
}
