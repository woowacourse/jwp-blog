package techcourse.myblog.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime date;
    @OneToOne
    private User author;
    private String contents;

    public Comment() {}

    public Comment(User author, String contents) {
        this.author = Optional.ofNullable(author).orElseThrow(IllegalArgumentException::new);
        this.contents = Optional.ofNullable(contents).orElseThrow(IllegalArgumentException::new);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public User getAuthor() {
        return this.author;
    }

    public String getContents() {
        return this.contents;
    }
}