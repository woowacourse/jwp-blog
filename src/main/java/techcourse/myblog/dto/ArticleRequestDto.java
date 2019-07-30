package techcourse.myblog.dto;

import javax.validation.constraints.NotNull;

public class ArticleRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String contents;
    private String coverUrl;

    public ArticleRequestDto(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public ArticleRequestDto() {
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
}