package techcourse.myblog.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentTest {
    private static final User user = User.builder()
            .userName("buddy")
            .email("buddy@gmail.com")
            .password("Aa12345!")
            .build();

    private static final Comment comment = Comment.builder()
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
        User user2 = new User("cony", "cony@cony.com", "Aa12345!");

        assertThrows(InvalidAuthorException.class, () -> comment.update(comment2, user2));
    }
}