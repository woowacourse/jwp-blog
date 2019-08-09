package techcourse.myblog.comment.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.data.ArticleDataForTest;
import techcourse.myblog.data.CommentDataForTest;
import techcourse.myblog.data.UserDataForTest;
import techcourse.myblog.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CommentTest {
    private final User author = User.builder()
            .id(1)
            .email(UserDataForTest.USER_EMAIL)
            .password(UserDataForTest.USER_PASSWORD)
            .name(UserDataForTest.USER_NAME)
            .build();

    private final Article article = Article.builder()
            .id(1)
            .contents(ArticleDataForTest.ARTICLE_CONTENTS)
            .coverUrl(ArticleDataForTest.ARTICLE_COVER_URL)
            .title(ArticleDataForTest.ARTICLE_TITLE)
            .author(author)
            .build();

    private final Comment comment = Comment.builder()
            .author(author)
            .article(article)
            .contents(CommentDataForTest.COMMENT_CONTENTS)
            .build();


    @Test
    void 업데이트() {
        comment.updateComment(CommentDataForTest.UPDATED_CONTENTS, commentId);
        assertThat(comment.getContents()).isEqualTo(CommentDataForTest.UPDATED_CONTENTS);
    }

    @Test
    void checkPassword() {
        assertFalse(comment.notMatchAuthorId(1));
    }
}
