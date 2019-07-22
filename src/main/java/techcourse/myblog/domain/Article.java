package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
    
    private Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }
    
    public static Article from(String title, String coverUrl, String contents) {
        return new Article(title, coverUrl, contents);
    }
    
    public void update(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
    }
}