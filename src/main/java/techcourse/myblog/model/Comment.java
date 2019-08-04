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
@RequiredArgsConstructor
@EqualsAndHashCode
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "CONTENTS", nullable = false, columnDefinition = "TEXT")
    @NonNull
    private String contents;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime currentDateTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ARTICLE_ID", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;


    public Comment update(Comment comment) {
        this.author = comment.getAuthor();
        this.contents = comment.getContents();
        this.article = comment.getArticle();

        return this;
    }

    public void updateArticle(Article article) {
        this.article = article;
        if (!article.getComments().contains(this)) {
            article.getComments().add(this);
        }
    }

    public void checkOwner(User user) {
        if (!this.author.equals(user)) {
            throw new MisMatchAuthorException("댓글을 작성한 유저만 수정할 수 있습니다.");
        }
    }
}
