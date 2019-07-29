package techcourse.myblog.domain;

import lombok.*;
import techcourse.myblog.service.dto.ArticleRequestDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String title;
    @NotNull
    private String contents;
    @NotNull
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
