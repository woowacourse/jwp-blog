package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(length = 100)
    private String title;

    @NonNull
    @Column(length = 1000)
    private String contents;

    @NonNull
    @Column(length = 100)
    private String coverUrl;

    @ManyToOne
    @NonNull
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void update(Article article) {
        this.title = article.title;
        this.contents = article.contents;
        this.coverUrl = article.coverUrl;
    }

    public void add(Comment comment) {
        comments.add(comment);
    }

    public void remove(Comment comment) {
        comments.remove(comment);
    }
}
