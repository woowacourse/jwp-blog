package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {
    static final User user = new User("TEST", "test@test.com", "test1234");
    static final Article article = new Article(user, "Title", "Background", "Content");
    static final Comment comment = new Comment(article, user, "salkgjabske;fas");

    @Test
    void isSameAuthor() {
        assertThat(comment.isSameAuthor(user)).isTrue();
    }
}