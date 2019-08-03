package techcourse.myblog.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.InvalidAuthorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static techcourse.myblog.user.UserTest.user;
import static techcourse.myblog.user.UserTest.user2;

public class CommentTest {
    public static final Comment comment = Comment.builder()
            .contents("댓글입니다.")
            .author(user)
            .build();

    private static final Comment comment2 = Comment.builder()
            .contents("수정된 댓글입니다.")
            .author(user)
            .build();


    @Test
    void 댓글_작성자_일치하고_수정() {
        comment.update(comment2, user);

        assertEquals(comment.getContents(), "수정된 댓글입니다.");
    }

    @Test
    void 댓글을_작성자_불일치() {
        assertThrows(InvalidAuthorException.class, () -> comment.update(comment2, user2));
    }
}