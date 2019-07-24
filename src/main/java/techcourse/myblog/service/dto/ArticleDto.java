package techcourse.myblog.service.dto;

import techcourse.myblog.domain.Article;

public class ArticleDto {
    private Long id;
    private Long userId;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleDto(Long id, Long userId, String title, String coverUrl, String contents) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Article toEntity() {
        return new Article(userId, title, coverUrl, contents);
    }
}
