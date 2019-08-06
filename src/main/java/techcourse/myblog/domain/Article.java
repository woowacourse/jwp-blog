package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.domain.exception.InvalidAccessException;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Getter
public class Article extends AuditLog {
    private static final String INVALID_ERROR = "작성자가 아닙니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "coverUrl", nullable = false)
    private String coverUrl;

    @Lob
    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"), nullable = false)
    private User author;

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void update(Article articleToUpdate, long loginUserId) {
        isAuthor(loginUserId);
        this.title = articleToUpdate.title;
        this.coverUrl = articleToUpdate.coverUrl;
        this.contents = articleToUpdate.contents;
    }

    public void setAuthor(User persistUser) {
        this.author = persistUser;
    }

    public void isAuthor(long loginUserId) {
        if (loginUserId == id) {
            return;
        }

        throw new InvalidAccessException(INVALID_ERROR);
    }
}