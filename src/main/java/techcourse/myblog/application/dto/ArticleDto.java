package techcourse.myblog.application.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public class ArticleDto {
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
    private User user;

    public ArticleDto(Long id, String title, String coverUrl, String contents, User user) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.user = user;
    }

    public static ArticleDto of(Article article) {
        return new ArticleDto(article.getId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents(),
                article.getUser()
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
