package techcourse.myblog.domain;

public class ArticleRequestDto {
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleRequestDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
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

    public Article toArticle() {
        return Article.createWithoutId(title, coverUrl, contents);
    }
}
