package techcourse.myblog.dto;

public class ArticleDto {
    private final String title;
    private final String contents;
    private final String coverUrl;

    public ArticleDto(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
