package techcourse.myblog.domain.comment;

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

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @ManyToOne
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

    protected Comment() {
    }

    public Comment(String contents) {
        this.contents = contents;
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

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isAuthor(String authorEmail) {
        return this.author.isMatchEmail(authorEmail);
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Comment updateContent(String contents) {
        this.contents = contents;
        return this;
    }
}
