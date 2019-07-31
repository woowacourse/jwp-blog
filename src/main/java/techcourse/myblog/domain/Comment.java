package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@EqualsAndHashCode(of = "id")
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private String contents;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    protected Comment() {}

    public Comment(String contents, User user, Article article) {
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    public void modify(String contents) {
        this.contents = contents;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public String getContents() {
        return contents;
    }

    public User getUser() {
        return user;
    }

    public Article getArticle() {
        return article;
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
}
