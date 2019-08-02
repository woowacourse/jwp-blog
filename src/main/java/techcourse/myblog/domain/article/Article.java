package techcourse.myblog.domain.article;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    private String coverUrl;

    @Column(nullable = false, length = 2000)
    private String contents;

    @Column(nullable = false)
    private long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    public Article() {
    }

    @Builder
    public Article(long id, String title, String coverUrl, String contents, long categoryId, User author) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.categoryId = categoryId;
        this.author = author;
    }

    public void update(Article article) {
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
        this.categoryId = article.getCategoryId();
    }

    public void setAuthor(User persistUser) {
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

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents, categoryId);
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
}
