package techcourse.myblog.domain;

import techcourse.myblog.web.dto.ArticleDto;

public class Article {
    private final int articleId;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(int articleId, String title, String coverUrl, String contents) {
        this.articleId = articleId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article(int articleId, ArticleDto articleDto) {
        this.articleId = articleId;
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
    }

    public boolean isSameId(int articleId) {
        return this.articleId == articleId;
    }

    public void updateArticle(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
    }

    public int getArticleId() {
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
}
