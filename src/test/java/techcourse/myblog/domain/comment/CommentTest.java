package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {
    private static final String BASE_COMMENT = "comment";

    private Comment comment;
    private User author;

    @BeforeEach
    public void setUp() {
        author = new User("name", "test@test.test", "passWORD1!");
        comment = new Comment(BASE_COMMENT, author, null);
    }

    @Test
    public void update() {
        String newComment = "new Comment";
        Comment updateComment = new Comment(newComment, author, null);

        comment.updateComment(updateComment);

        assertThat(comment.getComment()).isEqualTo(newComment);
    }

    @Test
    @DisplayName("유저가 다르면 Comment 수정 실패")
    public void updateFail() {
        String newComment = "new Comment";
        User other = new User("name", "other@test.test", "passWORD1!");
        Comment updateComment = new Comment(newComment, other, null);

        comment.updateComment(updateComment);

        assertThat(comment.getComment()).isEqualTo(BASE_COMMENT);
    }
}