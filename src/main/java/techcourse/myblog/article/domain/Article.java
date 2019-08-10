package techcourse.myblog.article.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.article.dto.ArticleUpdateDto;
import techcourse.myblog.user.domain.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 30)
    private String title;

    @Column(length = 100)
    private String coverUrl;

    private String contents;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @Builder
    private Article(long id, String title, String coverUrl, String contents, User author) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    public Article update(ArticleUpdateDto articleUpdateDto) {
        this.title = articleUpdateDto.getTitle();
        this.coverUrl = articleUpdateDto.getCoverUrl();
        this.contents = articleUpdateDto.getContents();

        return this;
    }
}
