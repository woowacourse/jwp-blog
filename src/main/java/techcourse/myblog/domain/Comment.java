package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.domain.exception.InvalidAccessException;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends AuditLog {
    private static final String INVALID_ERROR = "잘못된 접근 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"), nullable = false)
    private User author;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"), nullable = false)
    private Article article;

    public Comment(String contents, User author, Article article) {
        this.contents = contents;
        this.author = author;
        this.article = article;
    }

    public void update(String contents, long loginUserId) {
        isAuthor(loginUserId);
        this.contents = contents;
    }

    public void isAuthor(long loginUserId) {
        if (loginUserId == author.getId()) {
            return;
        }

        throw new InvalidAccessException(INVALID_ERROR);
    }
}
