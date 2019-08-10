package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.exception.CommentUpdateFailedException;
import techcourse.myblog.domain.exception.InvalidCommentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentTest {
    private User author = new User("author", "author@mail.com", "Passw0rd!");
    private Article article = new Article("title", "coverUrl", "contents", author);
    private Comment originComment = new Comment("comment", author, article);

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
    void 댓글_업데이트_성공() {
        Comment editedComment = new Comment("수정됨", author, article);

        assertDoesNotThrow(() -> originComment.update(editedComment));

        assertThat(originComment.getContents()).isEqualTo(editedComment.getContents());
    }

    @Test
    void 댓글_업데이트_불가1() {
        assertThrows(CommentUpdateFailedException.class, () ->
                originComment.update(null));
    }

    @Test
    void 댓글_업데이트_불가2() {
        User notAuthor = new User("notAuthor", "not@mail.com", "Passw0rd!");
        Comment editedComment = new Comment("edited", notAuthor, article);
        assertThrows(CommentUpdateFailedException.class, () ->
                originComment.update(editedComment));
    }
}
