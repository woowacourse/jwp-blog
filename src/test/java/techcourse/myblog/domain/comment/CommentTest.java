package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.InvalidAuthorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static techcourse.myblog.domain.user.UserTest.user;
import static techcourse.myblog.domain.user.UserTest.user2;

public class CommentTest {
    public static final Comment comment = Comment.builder()
            .contents("댓글입니다.")
            .author(user)
            .build();


    @Test
    void 댓글_작성자_일치하고_수정() {
        Comment comment2 = Comment.builder()
                .contents("수정된 댓글입니다.")
                .author(user)
                .build();
        comment.update(comment2);

        assertEquals(comment.getContents(), "수정된 댓글입니다.");
    }

    @Test
    void 댓글을_작성자_불일치() {
        Comment comment2 = Comment.builder()
                .contents("수정된 댓글입니다.")
                .author(user2)
                .build();
        assertThrows(InvalidAuthorException.class, () -> comment.update(comment2));
    }
}