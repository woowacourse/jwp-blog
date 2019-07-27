package techcourse.myblog.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate localDate;
    private LocalTime localTime;
    private String contents;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    public Comment(String contents, User user, Article article) {
        localDate = LocalDate.now();
        localTime = LocalTime.now();
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    public long getId() {
        return id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
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

    public void modify(String contents) {
        this.contents = contents;
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
