package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    private static final int EMPTY_ID = -1;

    private final int id;
    private final String title;
    private final String coverUrl;
    private final String contents;

    public Article(int id, String title, String coverUrl, String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public static Article createWithoutId(String title, String coverUrl, String contents) {
        return new Article(EMPTY_ID, title, coverUrl, contents);
    }

    public int getId() {
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
        return id == article.id &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
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

    public Article replaceId(int id) {
        return new Article(id, title, coverUrl, contents);
    }

    public boolean isEqualToExceptId(Article article) {
        return Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }
}
