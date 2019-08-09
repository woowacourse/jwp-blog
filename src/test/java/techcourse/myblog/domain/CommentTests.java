package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.vo.CommentContents;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTests {
    @Test
    void updateContent_test() {
        User user = new User("kangmin789@naver.com", "kangmin", "asdASD12!@");
        Article article = new Article("title", "coverUrl", "content");
        Comment basicComment = new Comment(new CommentContents("contents"), user, article);
        Comment expectUpdateComment = new Comment(new CommentContents("updateContents"), user, article);
        basicComment.updateContents(expectUpdateComment.getContents());

        assertThat(basicComment.getCommentContents()).isEqualTo(expectUpdateComment.getCommentContents());
    }
}
