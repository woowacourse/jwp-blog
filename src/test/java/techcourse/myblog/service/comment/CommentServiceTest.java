package techcourse.myblog.service.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.service.dto.comment.CommentRequest;
import techcourse.myblog.service.dto.comment.CommentResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CommentServiceTest {
    private static final Long DEFAULT_USER_ID = 999L;
    private static final Long DEFAULT_ARTICLE_ID = 999L;
    private static final Long DEFAULT_COMMENT_ID = 999L;
    private static final int AUTO_INCREMENT_ID = 1;


    @Autowired
    private CommentService commentService;

    @Test
    void 댓글_작성() {
        CommentRequest commentDto = new CommentRequest("comment");
        CommentResponse persistComment = commentService.save(commentDto, DEFAULT_USER_ID, DEFAULT_ARTICLE_ID);
        assertThat(persistComment).isEqualTo(new CommentResponse(DEFAULT_COMMENT_ID + AUTO_INCREMENT_ID, "comment", DEFAULT_USER_ID, "john"));
    }

    @Test
    void 댓글_수정() {
        CommentRequest commentDto = new CommentRequest("new Hello");
        CommentResponse updateComment = commentService.update(commentDto, DEFAULT_COMMENT_ID);
        assertThat(updateComment).isEqualTo(new CommentResponse(DEFAULT_COMMENT_ID, "new Hello", 1000L, "paul"));
    }

    @Test
    void 댓글_삭제() {
        commentService.delete(DEFAULT_COMMENT_ID);
        assertThatExceptionOfType(CommentNotFoundException.class)
                .isThrownBy(() -> commentService.findById(DEFAULT_COMMENT_ID));
    }
}
