package techcourse.myblog.domain.article;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String coverUrl;
    private String contents;
    private long categoryId;

    public Article() {
    }

    public Article(long id, String title, String coverUrl, String contents, long categoryId) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id &&
                categoryId == article.categoryId &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents, categoryId);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
