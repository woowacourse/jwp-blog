package techcourse.myblog.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.MisMatchAuthorException;

import static org.assertj.core.api.Assertions.*;

class CommentTest {
    private Comment testComment;
    private User author;
    private Article article;

    @BeforeEach
    void setUp() {
        author = new User("owner", "owner@email.com", "!@QW12qw12");
        article = new Article();
        testComment = new Comment("contents", author, article);
    }

    @Test
    @DisplayName("댓글을 업데이트 합니다")
    void update() {
        Comment updatedComment = new Comment("updated contents", author, article);

        testComment.update(updatedComment);
        assertThat(testComment.getContents()).isEqualTo(updatedComment.getContents());
    }

    @Test
    @DisplayName("임의의 사용자가 댓글을 작성한 사용자인지 확인합니다.")
    void checkOnwer() {
        User stranger = new User("stranger", "stranger@email.com", "!@QW12qw");

        assertThatThrownBy(() -> testComment.checkOwner(stranger)).isInstanceOf(MisMatchAuthorException.class);
        assertThatCode(() -> testComment.checkOwner(author)).doesNotThrowAnyException();
    }
}