package techcourse.myblog.domain;

import lombok.*;
import techcourse.myblog.service.dto.ArticleRequestDto;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 2000)
    private String contents;
    @Column(nullable = false)
    private String coverUrl;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AUTHOR"), name = "author")
    private User author;

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public void update(ArticleRequestDto articleRequestDto) {
        this.title = articleRequestDto.getTitle();
        this.contents = articleRequestDto.getContents();
        this.coverUrl = articleRequestDto.getCoverUrl();
    }

    public void setAuthor(User persistUser) {
        this.author = persistUser;
    }
}
