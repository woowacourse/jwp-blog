package techcourse.myblog.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long articleId;

    @Column(nullable = false)
    private String title;

    private String coverUrl;
    private String contents;

    public Article() {
    }

    public Article(long articleId, String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.articleId = articleId;
    }

    public Article update(Article updatedArticle) {
        this.title = updatedArticle.getTitle();
        this.coverUrl = updatedArticle.getCoverUrl();
        this.contents = updatedArticle.getContents();
        return this;
    }

    public long getArticleId() {
        return articleId;
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
        return articleId == article.articleId &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, title, coverUrl, contents);
    }
}
