package techcourse.myblog.domain;

public class ArticleVO {

    private final String title;
    private final String coverUrl;
    private final String contents;

    public ArticleVO(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
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
}
