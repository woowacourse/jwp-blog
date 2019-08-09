package techcourse.myblog.domain.comment;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @CreationTimestamp
    private LocalDateTime createdTimeAt;
    @UpdateTimestamp
    private LocalDateTime updateTimeAt;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Article article;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User author;
    private String contents;

    public Comment() {}

    public Comment(Article article, User author, String contents) {
        this.article = Optional.ofNullable(article).orElseThrow(IllegalArgumentException::new);
        this.author = Optional.ofNullable(author).orElseThrow(IllegalArgumentException::new);
        this.contents = Optional.ofNullable(contents).orElseThrow(IllegalArgumentException::new);
    }

    public long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedTimeAt() {
        return this.createdTimeAt;
    }

    public LocalDateTime getUpdateTimeAt() {
        return this.updateTimeAt;
    }

    public User getAuthor() {
        return this.author;
    }

    public boolean isSameAuthor(User user) {
        return this.author.equals(user);
    }

    public String getContents() {
        return this.contents;
    }

    public Comment setContents(String contents){
        this.contents = contents;
        return this;
    }
}