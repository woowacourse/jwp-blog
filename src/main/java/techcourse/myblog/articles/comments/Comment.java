package techcourse.myblog.articles.comments;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import techcourse.myblog.articles.Article;
import techcourse.myblog.users.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comment_to_user"), nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="article_id",foreignKey = @ForeignKey(name = "fk_comment_to_article"), nullable = false)
    private Article article;

    @Builder
    public Comment(final String contents, final User user, final Article article) {
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    public void update(final Comment other) {
        this.contents = other.contents;
    }

    public boolean isWrittenBy(final User other) {
        return this.user.equals(other);
    }
}
