package techcourse.myblog.domain.comment;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commenter_id", foreignKey = @ForeignKey(name = "fk_comment_to_user"), nullable = false)
    private User commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_comment_to_article"), nullable = false)
    private Article article;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    public Comment() {
    }

    public Comment(String contents) {
        this.contents = contents;
    }

    public Comment(String contents, User commenter, Article article) {
        this.contents = contents;
        this.commenter = commenter;
        this.article = article;
    }

    public void update(Comment comment) {
        this.contents = comment.contents;
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public User getCommenter() {
        return commenter;
    }

    public Article getArticle() {
        return article;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
