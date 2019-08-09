package techcourse.myblog.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.exception.MisMatchAuthorException;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @NonNull
    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    private String contents;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime currentDateTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

    public Comment update(Comment comment) {
        this.author = comment.getAuthor();
        this.contents = comment.getContents();
        this.article = comment.getArticle();

        return this;
    }

    public void checkOwner(User user) {
        if (!this.author.equals(user)) {
            throw new MisMatchAuthorException("댓글을 작성한 유저만 수정할 수 있습니다.");
        }
    }
}
