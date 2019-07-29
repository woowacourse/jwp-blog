package techcourse.myblog.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //    private LocalDateTime date;
    @ManyToOne
    private Article article;

    @ManyToOne
    private User author;

    private String contents;


    public Comment() {
    }

    public Comment(Article article, User author, String contents) {
        this.article = Optional.ofNullable(article).orElseThrow(IllegalArgumentException::new);
        this.author = Optional.ofNullable(author).orElseThrow(IllegalArgumentException::new);
        this.contents = Optional.ofNullable(contents).orElseThrow(IllegalArgumentException::new);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getAuthor() {
        return this.author;
    }

    public String getContents() {
        return this.contents;
    }
}