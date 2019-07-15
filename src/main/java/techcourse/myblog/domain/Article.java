package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    private static final String DEFAULT_COVER_URL = "/images/pages/index/study.jpg";
    private static final int NO_COVER_URL = 0;
    private static long NEXT_ID = 1L;

    private long articleId;
    private String title;
    private String contents;
    private String coverUrl;

    public Article() {
    }

    public Article(final long articleId, final String title, final String contents, final String coverUrl) {
        this.articleId = articleId == 0 ? NEXT_ID++ : articleId;
        this.title = title;
        this.contents = contents;
        this.coverUrl = (coverUrl.length() != NO_COVER_URL) ? coverUrl : DEFAULT_COVER_URL;
    }

    public boolean matchId(long articleId) {
        return this.articleId == articleId;
    }

    public void update(Article updatedArticle) {
        this.articleId = updatedArticle.getArticleId();
        this.title = updatedArticle.getTitle();
        this.contents = updatedArticle.getContents();
        this.coverUrl = updatedArticle.getCoverUrl();
    }

    public long getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }


    public String getCoverUrl() {
        return coverUrl;
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
