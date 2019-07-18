package techcourse.myblog.web.dto;

public class ArticleDto {
    private String title;
    private String coverUrl;
    private String contents;

    public static ArticleDto of(String title, String coverUrl, String contents) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(title);
        articleDto.setCoverUrl(coverUrl);
        articleDto.setContents(contents);
        return articleDto;
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
