package techcourse.myblog.dto;

import techcourse.myblog.domain.Article;

public class ArticleDto {

    private String title;
    private String contents;
    private String coverUrl;

    public ArticleDto(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public ArticleDto() {
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
        this.coverUrl = coverUrl;
    }

    public Article toEntity() {
        return new Article(title, contents, coverUrl);
    }
}
