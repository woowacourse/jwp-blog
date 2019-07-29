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
    @JoinColumn(name="writer", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    User writer;
    String contents;
    @CreationTimestamp
    private LocalDateTime createdTimeAt;

    public Comment() {}

    public Comment(User writer, String contents) {
        this.writer = writer;
        this.contents = contents;
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

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(writer, comment.writer) &&
                Objects.equals(contents, comment.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, contents);
    }
}