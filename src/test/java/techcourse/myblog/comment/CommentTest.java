package techcourse.myblog.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.user.User;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentTest {
    @Test
    void 댓글_작성자_확인() {
        User user1 = new User("buddy", "buddy@buddy.com", "Aa12345!");
        User user2 = new User("cony", "cony@cony.com", "Aa12345!");

        Comment comment = new Comment(user1, "댓글입니다");

        assertDoesNotThrow(() -> comment.checkCorrespondingAuthor(user1));
        assertThrows(InvalidAuthorException.class, () -> comment.checkCorrespondingAuthor(user2));
    }
}