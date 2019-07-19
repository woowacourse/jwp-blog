package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    private static Long NEXT_ARTICLE_ID = 1L;

    private Long articleId;
    private String title;
    private String contents;
    private String coverUrl;

    public Article(String title, String contents, String coverUrl) {
        this.articleId = NEXT_ARTICLE_ID++;
        this.title = title;
        this.contents = contents;
        this.coverUrl = (coverUrl.length() == 0) ? "/images/pages/index/study.jpg" : coverUrl;
    }

    public Long getArticleId() {
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

    void update(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;

        NEXT_ARTICLE_ID--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(articleId, article.articleId) &&
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
