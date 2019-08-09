package techcourse.myblog.comment.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.user.domain.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 200)
    private String contents;

    @ManyToOne
    private User author;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    @Builder
    private Comment(String contents, User author, Article article) {
        this.contents = contents;
        this.author = author;
        this.article = article;
    }

    public Comment updateComment(String contents, long authorId) {
        if (author.checkId(authorId)) {
            this.contents = contents;
            return this;
        }
        throw new NotMatchUserException(authorId);
    }

    public boolean notMatchAuthorId(long authorId) {
        return !author.checkId(authorId);
    }

    public long getUserId() {
        return author.getId();
    }

    public String getUserName() {
        return author.userName();
    }
}
