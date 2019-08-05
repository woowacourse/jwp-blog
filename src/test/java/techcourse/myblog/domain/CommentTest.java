package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.NotMatchCommentAuthorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentTest {
    private static final Long USER_ID = 1L;
    private static final String EMAIL = "aa@naver.com";
    private static final String NAME = "zino";
    private static final String PASSWORD = "password";
    private static final Long ARTICLE_ID = 2L;
    private static final String TITLE = "ThisIsTitle";
    private static final String COVER_URL = "ThisIsCoverUrl";
    private static final String CONTENTS = "ThisIsContents";
    private static final Long COMMENT_ID = 3L;
    private static final String COMMENT_CONTENTS = "ThisIsCommentContents";


    @Test
    void 수정_테스트() {
        User user = new User(USER_ID, EMAIL, NAME, PASSWORD);
        Article article = new Article(ARTICLE_ID, TITLE, COVER_URL, CONTENTS, user);
        Comment comment = new Comment(COMMENT_ID, COMMENT_CONTENTS, user, article);

        CommentDto commentDto = new CommentDto(null, COMMENT_CONTENTS + "abc", null);
        comment.modify(commentDto, user);

        assertThat(comment.getContents()).isEqualTo(COMMENT_CONTENTS + "abc");
    }

    @Test
    void 작성자와_다른_유저의_수정_에러_테스트() {
        User user = new User(USER_ID, EMAIL, NAME, PASSWORD);
        Article article = new Article(ARTICLE_ID, TITLE, COVER_URL, CONTENTS, user);
        Comment comment = new Comment(COMMENT_ID, COMMENT_CONTENTS, user, article);
        User anotherUser = new User(USER_ID + 1L, EMAIL + "a", NAME + "a", PASSWORD + "a");

        CommentDto commentDto = new CommentDto(null, COMMENT_CONTENTS + "abc", null);
        assertThrows(NotMatchCommentAuthorException.class, () -> comment.modify(commentDto, anotherUser));
    }
}