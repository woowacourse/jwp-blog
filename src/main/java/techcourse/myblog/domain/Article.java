package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String contents;

    @NonNull
    private String coverUrl;

    @ManyToOne
    private User author;

    public void setAuthor(User user) {
        this.author = user;
    }

    public void update(Article article) {
        this.title = article.title;
        this.contents = article.contents;
        this.coverUrl = article.coverUrl;
    }
}
