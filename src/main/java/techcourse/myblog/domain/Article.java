package techcourse.myblog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article() {}
    
    private Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }
    
    public static Article to(String title, String coverUrl, String contents) {
        return new Article(title, coverUrl, contents);
    }
    
    public void update(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
    
    public String getContents() {
        return contents;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}