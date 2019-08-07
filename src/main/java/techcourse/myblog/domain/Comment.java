package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Getter
public class Comment extends BaseEntity {
    @Column(nullable = false, length = 600)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writer;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    public Comment(String contents, User writer, Article article) {
        validateContents(contents);
        this.contents = contents;
        this.writer = writer;
        this.article = article;
    }

    private void validateContents(String contents) {
        if (contents == null || contents.isEmpty()) {
            throw new InvalidCommentException();
        }
    }

    public Comment update(Comment comment) {
        if (comment == null) {
            throw new CommentUpdateFailedException();
        }

        checkAuth(comment);
        this.contents = comment.contents;

        return this;
    }

    private void checkAuth(Comment comment) {
        if (matchArticle(comment.article) && matchWriter(comment.writer)) {
            return;
        }
        throw new CommentUpdateFailedException();
    }

    private boolean matchArticle(Article article) {
        return (article != null && this.article.equals(article));
    }

    public boolean matchWriter(User user) {
        return user != null && writer.equals(user);
    }
}
