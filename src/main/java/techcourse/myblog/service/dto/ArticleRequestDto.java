package techcourse.myblog.service.dto;

import techcourse.myblog.domain.Article;

public class ArticleRequestDto {

    private String title;
    private String coverUrl;
    private String contents;

    public ArticleRequestDto() {
    }

    public Article toEntity() {
        return new Article(title, coverUrl, contents);
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
