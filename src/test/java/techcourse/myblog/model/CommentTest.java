package techcourse.myblog.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.MisMatchAuthorException;

import static org.assertj.core.api.Assertions.*;

class CommentTest {
    private static final String USER_NAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "passWord!1";

    private static final String WRONG_USER_NAME = "newtest";
    private static final String WRONG_EMAIL = "test2@test.com";
    private static final String WRONG_PASSWORD = "passWord!12";

    private static final String COMMENT_CONTENTS = "COMMENT";
    private static final String COMMENT_CONTENTS_2 = "COMMENT2";

    private Comment testComment;
    private User author;
    private Article article;

    @BeforeEach
    void setUp() {
        author = new User(USER_NAME, EMAIL, PASSWORD);
        article = new Article();
        testComment = new Comment(COMMENT_CONTENTS, author, article);
    }

    @Test
    @DisplayName("댓글을 업데이트 합니다")
    void update() {
        Comment updatedComment = new Comment(COMMENT_CONTENTS_2, author, article);

        testComment.update(updatedComment);
        assertThat(testComment.getContents()).isEqualTo(updatedComment.getContents());
    }

    @Test
    @DisplayName("임의의 사용자가 댓글을 작성한 사용자인지 확인합니다.")
    void checkOnwer() {
        User stranger = new User(WRONG_USER_NAME, WRONG_EMAIL, WRONG_PASSWORD);

        assertThatThrownBy(() -> testComment.checkOwner(stranger)).isInstanceOf(MisMatchAuthorException.class);
        assertThatCode(() -> testComment.checkOwner(author)).doesNotThrowAnyException();
    }
}