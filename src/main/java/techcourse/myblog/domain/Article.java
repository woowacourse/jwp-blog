package techcourse.myblog.domain;

public class Article {
    private static int nextId = 1;

    private int id;
    private String title;
    private String coverUrl;
    private String contents;

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = nextId++;
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

    public void update(Article article) {
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
    }
}
