package techcourse.myblog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
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
    @Column(nullable = false)
    private Article article;

    @ManyToOne
    @Column(nullable = false)
    private User author;

    @Column(nullable = false, length = 500)
    private String contents;

    public Comment(Article article, User author, String contents) {
        this.article = Optional.ofNullable(article).orElseThrow(IllegalArgumentException::new);
        this.author = Optional.ofNullable(author).orElseThrow(IllegalArgumentException::new);
        this.contents = Optional.ofNullable(contents).orElseThrow(IllegalArgumentException::new);
    }

    public boolean isSameAuthor(User user) {
        return this.author.equals(user);
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}