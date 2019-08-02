package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String contents;
    private String coverUrl;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    public Article(String title, String contents, String coverUrl, User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }
}
