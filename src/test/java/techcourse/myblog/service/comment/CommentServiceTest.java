package techcourse.myblog.service.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.exception.CommentNotFoundException;

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
        Comment persistComment = commentService.save(commentDto, DEFAULT_USER_ID, DEFAULT_ARTICLE_ID);
        assertThat(persistComment.getContents()).isEqualTo(commentDto.getContents());
    }

    @Test
    void 댓글_수정() {
        CommentRequestDto commentDto = new CommentRequestDto("new Hello");
        Comment updateComment = commentService.update(commentDto, DEFAULT_COMMENT_ID);
        assertThat(updateComment.getContents()).isEqualTo("new Hello");
    }

    @Test
    void 댓글_삭제() {
        commentService.delete(DEFAULT_COMMENT_ID);
        assertThatExceptionOfType(CommentNotFoundException.class)
            .isThrownBy(() -> commentService.findById(DEFAULT_COMMENT_ID));
    }
}
