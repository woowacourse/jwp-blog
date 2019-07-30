package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import techcourse.myblog.dto.CommentDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="writer", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User writer;

    private String contents;

    @CreationTimestamp
    private LocalDateTime createdTimeAt;

    @ManyToOne
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_article_to_comment"))
    private Article article;

    public Comment() {}

    public Comment(final User writer, final Article article, final String contents) {
        this.writer = writer;
        this.article = article;
        this.contents = contents;
        this.createdTimeAt = LocalDateTime.now();
    }

    public Comment(final CommentDto dto) {
        this.writer = dto.getWriter();
        this.article = dto.getArticle();
        this.contents = dto.getContents();
        this.createdTimeAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedTimeAt() {
        return createdTimeAt;
    }

    public Article getArticle() {
        return article;
    }

    public Comment update(final CommentDto dto) {
        this.contents = dto.getContents();
        return this;
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (!(another instanceof Comment)) return false;
        final Comment comment = (Comment) another;
        return id.equals(comment.id) &&
                writer.equals(comment.writer) &&
                article.equals(comment.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, article);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Comment.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("writer=" + writer)
                .add("contents='" + contents + "'")
                .add("createdTimeAt=" + createdTimeAt)
                .add("article=" + article)
                .toString();
    }

    public Long getArticleId() {
        return this.article.getId();
    }
}
