package techcourse.myblog.domain.coment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {
    @Test
    void 댓글_수정() {
        Comment comment = new Comment("comment ~~~",
                new User("aaa@example.com", "john", "p@ssW0rd"),
                new Article("title", "", "content",
                        new User("bbb@example.com", "james", "p@ssW0rd")));

        comment.update("changed comment");

        assertThat(comment.getContents()).isEqualTo("changed comment");
    }
}
