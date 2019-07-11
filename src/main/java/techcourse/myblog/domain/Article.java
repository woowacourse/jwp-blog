package techcourse.myblog.domain;

public class Article {
    private final int articleId;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(String title, String coverUrl, String contents, int articleId) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.articleId = articleId;
    }

    public int getArticleId() {
        return articleId;
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
