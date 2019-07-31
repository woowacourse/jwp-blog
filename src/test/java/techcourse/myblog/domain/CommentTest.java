package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.support.exception.InvalidCommentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentTest {
    User author = new User("abc", "eee@main.com", "author123!A@");
    Article article = new Article("title", "coverURl", "conetnts", author);

    @Test
    void 댓글_정상_생성() {
        assertDoesNotThrow(() ->
                new Comment("comment", author, article));
    }

    @Test
    void 댓글_생성_불가1() {
        assertThrows(InvalidCommentException.class, () ->
                new Comment("", author, article));
    }

    @Test
    void 댓글_생성_불가2() {
        assertThrows(InvalidCommentException.class, () ->
                new Comment(null, author, article));
    }

    @Test
    void 댓글_업데이트() {
        Comment editedComment = new Comment("수정됨", author, article);
        Comment originalComment = new Comment("원본", author, article);

        originalComment.update(editedComment);

        assertThat(originalComment.getContents()).isEqualTo(editedComment.getContents());
    }

    @Test
    void 댓글_업데이트_불가1() {
        Comment originalComment = new Comment("원본", author, article);
        assertThrows(InvalidCommentException.class, () ->
                originalComment.update(null));
    }
}
