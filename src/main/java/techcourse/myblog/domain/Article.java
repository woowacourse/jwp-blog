package techcourse.myblog.domain;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Article {

    private static AtomicLong incrementNumber = new AtomicLong();

    private Long articleId;
    private String title;
    private String coverUrl;
    private String contents;


    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.articleId = incrementNumber.incrementAndGet();
    }

    public void update(ArticleVO articleVO) {
        this.title = articleVO.getTitle();
        this.contents = articleVO.getContents();
        this.coverUrl = articleVO.getCoverUrl();

    }

    public boolean isEqualToArticleId(Long articleId) {
        return this.articleId == articleId;
    }

    public Long getArticleId() {
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
        return Objects.equals(articleId, article.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId);
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
