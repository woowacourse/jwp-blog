package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(long id, String title, String coverUrl, String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public boolean isSameId(final long id) {
        return (this.id == id);
    }

    public void update(final Article article) {
        this.id = article.id;
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
    }

    public ArticleDto toDto() {
        return new ArticleDto(id, title, coverUrl, contents);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return id == article.id &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
