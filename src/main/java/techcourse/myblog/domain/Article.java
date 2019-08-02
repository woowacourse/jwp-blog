package techcourse.myblog.domain;

import lombok.*;
import techcourse.myblog.dto.ArticleDto;

import javax.persistence.*;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    private String coverUrl;
    @ManyToOne
    private User author;

    public Article(String title, String contents, String coverUrl, User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    public boolean isNotMatchAuthor(User target) {
        return author.getId() != target.getId();
    }

    public void update(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.contents = articleDto.getContents();
        this.coverUrl = articleDto.getCoverUrl();
    }

    public boolean isNotMatch(long id) {
        return !this.id.equals(id);
    }
}
