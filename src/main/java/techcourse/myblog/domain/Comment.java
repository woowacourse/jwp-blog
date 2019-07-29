package techcourse.myblog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @ManyToOne
    private User user;
    @ManyToOne
    private Article article;

    public void initialize(final User user, final Article article) {
        this.user = user;
        this.article = article;
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
