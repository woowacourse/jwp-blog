package techcourse.myblog.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.MisMatchAuthorException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ArticleTest {
    private static final String USER_NAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "passWord!1";

    private static final String TITLE = "TITLE";
    private static final String COVER_URL = "cover_url";
    private static final String CONTENTS = "contents";

    private static final String COMMENT_CONTENTS = "COMMENT";
    private static final String COMMENT_CONTENTS_2 = "COMMENT2";

    private static final User USER = new User(USER_NAME, EMAIL, PASSWORD);
    private Comment comment;
    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article(TITLE, COVER_URL, CONTENTS, USER);
        comment = new Comment(COMMENT_CONTENTS, USER, article);

        article.addComment(comment);
    }

    @Test
    @DisplayName("게시글이 가지고 있는 코멘트 리스트에 새로운 코멘트를 더한다.")
    void addComment() {
        int beforeCommentSize = article.getComments().size();
        Comment newComment = new Comment(COMMENT_CONTENTS_2, USER, article);
        article.addComment(newComment);

        List<Comment> comments = article.getComments();
        assertThat(comments.get(beforeCommentSize)).isEqualTo(newComment);
        assertThat(comments.size()).isEqualTo(beforeCommentSize + 1);
    }

    @Test
    @DisplayName("게시글에 달린 댓글을 삭제한다.")
    void deleteComment() {
        article.deleteComment(comment);

        List<Comment> comments = article.getComments();
        assertThat(comments).doesNotContain(comment);
        assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글을 수정한다.")
    void updateArticle() {
        User author = new User();
        Article updatedArticle = new Article(
                "updated title",
                "updated coverUrl",
                "updated contents",
                author
        );

        article.update(updatedArticle);

        assertThat(article.getTitle()).isEqualTo(updatedArticle.getTitle());
        assertThat(article.getCoverUrl()).isEqualTo(updatedArticle.getCoverUrl());
        assertThat(article.getContents()).isEqualTo(updatedArticle.getContents());
    }

    @Test
    @DisplayName("어떤 임의의 사용자가 해당 게시글을 작성한 사용자가 아니면 예외를 던진다.")
    void checkOwner() {
        User author = new User("owner", "owner@email.com", "!@QW12qw12");
        User stranger = new User("stranger", "stranger@email.com", "!@QW12qw");
        Article article = new Article("title", "coverUrl", "contents", author);

        assertThatThrownBy(() -> article.checkOwner(stranger)).isInstanceOf(MisMatchAuthorException.class);
        assertThatCode(() -> article.checkOwner(author)).doesNotThrowAnyException();
    }
}
