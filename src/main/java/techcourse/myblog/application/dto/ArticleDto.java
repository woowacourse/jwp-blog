package techcourse.myblog.application.dto;

import techcourse.myblog.domain.Article;

public class ArticleDto {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleDto(long id, String title, String coverUrl, String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public static ArticleDto of(Article article) {
        return new ArticleDto(article.getId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents()
                );
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
