package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.web.dto.CommentDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @Column(nullable = false)
    private String contents;

    @CreationTimestamp
    private LocalDateTime createdTimeAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_article_to_comment"))
    private Article article;

    public Comment() {
    }

    public Comment(User author, String contents, Article article) {
        this.author = author;
        this.contents = contents;
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getContents() {
        return contents;
    }

    public Long getArticleId() {
        return article.getId();
    }

    public LocalDateTime getCreatedTimeAt() {
        return createdTimeAt;
    }

    public Comment update(CommentDto commentDto) {
        this.contents = commentDto.getContents();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(author, comment.author) &&
                Objects.equals(contents, comment.contents) &&
                Objects.equals(createdTimeAt, comment.createdTimeAt) &&
                Objects.equals(article, comment.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, contents, createdTimeAt, article);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author=" + author +
                ", contents='" + contents + '\'' +
                ", createdTimeAt=" + createdTimeAt +
                ", article=" + article +
                '}';
    }
}