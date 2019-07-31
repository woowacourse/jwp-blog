package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentTests {
    @Test
    void 자신이_작성한_댓글인지_확인() {
        User user = mock(User.class);
        Article article = mock(Article.class);
        Comment comment = new Comment("comment", user, article);

        when(user.getId()).thenReturn(1L);

        assertThat(comment.getAuthorId()).isEqualTo(user.getId());
    }
}
