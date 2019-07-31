package techcourse.myblog.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import techcourse.myblog.exception.NotMatchAuthenticationException;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Comment extends EntityDates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID", foreignKey = @ForeignKey(name = "FK_ARTICLE_TO_COMMENT"))
    private Article article;

    @Builder
    public Comment(final String content, final User user, final Article article) {
        this.content = content;
        this.user = user;
        this.article = article;
    }

    public Comment update(final String content, final User user) {
        authorizeFor(user);
        this.content = content;
        return this;
    }

    public void authorizeFor(User user) {
        if (!this.user.equals(user)) {
            throw new NotMatchAuthenticationException("권한이 없습니다.");
        }
    }
}
