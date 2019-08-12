package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.exception.UserMismatchException;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static techcourse.myblog.Utils.TestConstants.BASE_USER_ID;
import static techcourse.myblog.Utils.TestConstants.MISMATCH_USER_ID;

public class CommentTests {
    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        article = mock(Article.class);

        when(user.getId()).thenReturn(BASE_USER_ID);
        when(user.matchId(BASE_USER_ID)).thenReturn(true);
        when(user.matchId(MISMATCH_USER_ID)).thenReturn(false);
    }

    @Test
    void 자신이_작성한_댓글인지_확인() {
        Comment comment = new Comment("comment", user, article);

        assertThat(comment.getAuthorId()).isEqualTo(BASE_USER_ID);
    }

    @Test
    void 자신이_작성한_댓글_수정() {
        Comment comment = new Comment("comment", user, article);

        comment.updateComment("update comment", BASE_USER_ID);

        assertThat(comment.getComment()).isEqualTo("update comment");
    }

    @Test
    void 자신이_작성한_댓글이_아니면_수정되지_않는다() {
        Comment comment = new Comment("comment", user, article);

        assertThatThrownBy(() -> comment.updateComment("update comment", MISMATCH_USER_ID))
                .isInstanceOf(UserMismatchException.class);
    }
}
