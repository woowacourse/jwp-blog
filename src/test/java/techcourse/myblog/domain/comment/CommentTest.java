package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.article.Article;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CommentTest {
    private User articleAuthor;
    private User commentAuthor;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        articleAuthor = new User("bbb@example.com", "james", "p@ssW0rd");
        article = new Article("title", "", "content", articleAuthor);
        commentAuthor = new User("aaa@example.com", "john", "p@ssW0rd");
        comment = new Comment("comment ~~~", commentAuthor, article);
    }

    @Test
    void 댓글작성자가_댓글_수정() {
        comment.update(new Comment("new comment ~~~", commentAuthor, article));
        assertThat(comment.getContents()).isEqualTo("new comment ~~~");
    }

    @Test
    void 댓글작성자가_아닌_회원이_댓글_수정() {
        assertThatExceptionOfType(UserHasNotAuthorityException.class)
                .isThrownBy(() -> comment.update(new Comment("new comment ~~~", articleAuthor, article)));
    }
}
