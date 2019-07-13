package techcourse.myblog.domain;

public class Article implements Comparable<Article> {
    private int id;
    private String title;
    private String coverUrl = "";
    private String contents;

    public Article() {
    }

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTo(Article rhs) {
        this.title = rhs.title;
        this.coverUrl = rhs.coverUrl;
        this.contents = rhs.contents;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int compareTo(Article rhs) {
        return this.id - rhs.id;
    }
}