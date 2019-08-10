package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.domain.vo.CommentContents;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CommentContents commentContents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    @CreationTimestamp
    private LocalDateTime createdTimeAt;
    @UpdateTimestamp
    private LocalDateTime updateTimeAt;

    private Comment() {
    }

    public Comment(CommentContents commentContents, User author, Article article) {
        this.commentContents = commentContents;
        this.author = author;
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public CommentContents getContents() {
        return commentContents;
    }

    public String getCommentContents() {
        return commentContents.getContents();
    }

    public User getAuthor() {
        return author;
    }

    public String getUserEmail() {
        return author.getEmail();
    }

    public Article getArticle() {
        return article;
    }

    public void updateContents(CommentContents commentContents) {
        this.commentContents = commentContents;
    }

    @Override
    public String toString() {
        return "Comment{" + commentContents.getContents() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getArticleId() {
        return article.getId();
    }
}


