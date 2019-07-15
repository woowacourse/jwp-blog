package techcourse.myblog.web;

public class ArticleRequestDto {
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleRequestDto() {

    }

    public static ArticleRequestDto of(String title, String coverUrl, String contents) {
        ArticleRequestDto dto = new ArticleRequestDto();
        dto.title = title;
        dto.coverUrl = coverUrl;
        dto.contents = contents;
        return dto;
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
