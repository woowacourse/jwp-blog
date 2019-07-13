package techcourse.myblog.domain;

import lombok.Builder;

import java.util.Objects;

@Builder
public class Article {
    private static long articleId = 1;

    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    private Article(long id, String title, String coverUrl, String contents) {
        this.id = id == 0 ? articleId++ : id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
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

    public boolean isEqualTo(long articleId) {
        return this.id == articleId;
    }

    public void updateArticle(Article updatedArticle) {
        this.title = updatedArticle.getTitle();
        this.coverUrl = updatedArticle.getCoverUrl();
        this.contents = updatedArticle.getContents();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
