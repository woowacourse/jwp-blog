package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    private static Long NEXT_ARTICLE_ID = 1L;

    private Long articleId;
    private String title;
    private String contents;
    private String coverUrl;

    public static Article of(String title, String contents, String coverUrl) {
        Article article = new Article();
        article.setArticleId(NEXT_ARTICLE_ID++);
        article.setTitle(title);
        article.setContents(contents);
        article.setCoverUrl(coverUrl);

        return article;
    }

    public static Article from(ArticleDto dto) {
        Article article = new Article();
        article.setArticleId(NEXT_ARTICLE_ID++);
        article.setTitle(dto.getTitle());
        article.setContents(dto.getContents());
        article.setCoverUrl(dto.getCoverUrl());

        return article;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
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
        this.coverUrl = (coverUrl.length() != 0) ? coverUrl : "/images/pages/index/study.jpg";
    }

    void update(Article article) {
        this.setTitle(article.title);
        this.setCoverUrl(article.coverUrl);
        this.setContents(article.contents);

        NEXT_ARTICLE_ID--;
    }

    boolean hasSameArticleId(Article article) {
        return this.articleId.equals(article.articleId);
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
