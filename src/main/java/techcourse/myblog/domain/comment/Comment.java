package techcourse.myblog.domain.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String contents;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    @JsonBackReference
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    @JsonBackReference
    private Article article;

    protected Comment() {
    }

    public Comment(String contents, Article article, User author) {
        this.contents = contents;
        this.author = author;
        this.article = article;
        article.addComment(this);
        author.addComment(this);
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public User getAuthor() {
        return author;
    }

    public boolean isAuthor(User author) {
        return this.author.isMatchEmail(author);
    }

    public Article getArticle() {
        return article;
    }

    public Comment updateContents(String contents, User author) {
        if (!isAuthor(author)) {
            throw new CommentException("FBI WARNING");
        }
        this.contents = contents;
        return this;
    }

    public Comment updateContents(Comment comment, User author) {
        return updateContents(comment.contents, author);
    }
}
