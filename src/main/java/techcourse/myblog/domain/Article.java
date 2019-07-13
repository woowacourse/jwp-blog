package techcourse.myblog.domain;

import java.util.Objects;

public class Article{

    private static long incrementNumber = 1;    //TODO
    private Long articleId;
    private String title;
    private String coverUrl;
    private String contents;


    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.articleId = incrementNumber++;
    }

    public void update(ArticleVO articleVO){
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

}
