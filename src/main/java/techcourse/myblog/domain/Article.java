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

    public Article update(ArticleDto articleDto, User author) {
        checkMatchAuthor(author);
        return update(articleDto);
    }

    public Article update(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.contents = articleDto.getContents();
        this.coverUrl = articleDto.getCoverUrl();
        return this;
    }

    public void checkMatchAuthor(User user) {
        if (isNotMatchAuthor(user)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isMatchAuthor(User target) {
        return author.equals(target);
    }

    boolean isNotMatch(long id) {
        return !this.id.equals(id);
    }

    private boolean isNotMatchAuthor(User target) {
        return !isMatchAuthor(target);
    }
}
