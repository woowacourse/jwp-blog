package techcourse.myblog.service.dto;

import techcourse.myblog.domain.Article;

public class ArticleUpdateDto {

    private Long id;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleUpdateDto() {
    }

    public Article toEntity() {
        return new Article(id, title, coverUrl, contents);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
