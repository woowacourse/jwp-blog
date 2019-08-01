package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.exception.InvalidAccessException;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"), nullable = false)
    private User author;

    public Comment(String contents, User author) {
        this.contents = contents;
        this.author = author;
    }

    public void update(String contents, long loginUserId) {
        isAuthor(loginUserId);
        this.contents = contents;
    }

    public void isAuthor(long loginUserId) {
        if (loginUserId == author.getId()) {
            return;
        }

        throw new InvalidAccessException("잘못된 접근입니다.");
    }
}
