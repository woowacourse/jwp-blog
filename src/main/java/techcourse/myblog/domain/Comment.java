package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import techcourse.myblog.web.dto.CommentDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "writer", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    User writer;
    String contents;
    @CreationTimestamp
    private LocalDateTime createdTimeAt;
    @ManyToOne
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_article_to_comment"))
    private Article article;

    public Comment() {
    }

    public Comment(User writer, String contents, Article article) {
        this.writer = writer;
        this.contents = contents;
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Comment update(CommentDto commentDto) {
        this.contents = commentDto.getContents();
        return this;
    }

    public Long getArticleId() {
        return article.getId();
    }

    public LocalDateTime getCreatedTimeAt() {
        return createdTimeAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(writer, comment.writer) &&
                Objects.equals(contents, comment.contents) &&
                Objects.equals(createdTimeAt, comment.createdTimeAt) &&
                Objects.equals(article, comment.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, contents, createdTimeAt, article);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", createdTimeAt=" + createdTimeAt +
                ", article=" + article +
                '}';
    }
}