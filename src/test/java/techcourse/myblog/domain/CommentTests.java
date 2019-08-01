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

public class CommentTests {
    private static final Long MATCH_USER_ID = 1L;
    private static final Long MISMATCH_USER_ID = 2L;

    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        article = mock(Article.class);

        when(user.getId()).thenReturn(MATCH_USER_ID);
        when(user.matchId(MATCH_USER_ID)).thenReturn(true);
        when(user.matchId(MISMATCH_USER_ID)).thenReturn(false);
    }

    @Test
    void 자신이_작성한_댓글인지_확인() {
        Comment comment = new Comment("comment", user, article);

        assertThat(comment.getAuthorId()).isEqualTo(MATCH_USER_ID);
    }

    @Test
    void 자신이_작성한_댓글_수정() {
        Comment comment = new Comment("comment", user, article);

        comment.updateComment("update comment", MATCH_USER_ID);

        assertThat(comment.getComment()).isEqualTo("update comment");
    }

    @Test
    void 자신이_작성한_댓글이_아니면_수정되지_않는다() {
        Comment comment = new Comment("comment", user, article);

        assertThatThrownBy(() -> comment.updateComment("update comment", MISMATCH_USER_ID))
                .isInstanceOf(UserMismatchException.class);
    }
}
