package techcourse.myblog.domain;

public class Article {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void updateTitle(Article updatedArticle) {
        this.title = updatedArticle.title;
    }

    public void updateUrl(Article updatedArticle) {
        this.coverUrl = updatedArticle.coverUrl;
    }

    public void updateContents(Article updatedArticle) {
        this.contents = updatedArticle.contents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
