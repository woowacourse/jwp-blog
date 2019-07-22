package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String contents;
    private String coverUrl;

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public void update(Article article) {
        this.title = article.title;
        this.contents = article.contents;
        this.coverUrl = article.coverUrl;
    }
}
