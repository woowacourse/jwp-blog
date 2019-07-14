package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    private static final String DEFAULT_COVER_URL = "/images/pages/index/study.jpg";
    private static final int NO_COVER_URL = 0;

    private long articleId;
    private String title;
    private String contents;
    private String coverUrl;

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = (coverUrl.length() != NO_COVER_URL) ? coverUrl : DEFAULT_COVER_URL;
    }

    public boolean matchId(long articleId) {
        return this.articleId == articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return articleId == article.articleId &&
                Objects.equals(title, article.title) &&
                Objects.equals(contents, article.contents) &&
                Objects.equals(coverUrl, article.coverUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, title, contents, coverUrl);
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }

}
