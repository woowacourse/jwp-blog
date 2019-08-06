package techcourse.myblog.domain.comment;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.exception.UserMismatchException;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {
    private static final int COMMENT_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = COMMENT_LENGTH)
    private String comment;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    private Comment() {
    }

    public Comment(String comment, User author, Article article) {
        this.comment = comment;
        this.author = author;
        this.article = article;
    }

    public boolean matchAuthorId(Long userId) {
        return this.author.matchId(userId);
    }

    public void updateComment(String comment, Long userId) {
        if (!author.matchId(userId)) {
            throw new UserMismatchException();
        }
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public Long getAuthorId() {
        return author.getId();
    }

    public String getAuthorName() {
        return author.getName();
    }

    public Long getArticleId() {
        return article.getId();
    }
}
