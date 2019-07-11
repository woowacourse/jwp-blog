package techcourse.myblog.domain;

public class Article implements Comparable<Article> {
    private int number;
    private String title;
    private String coverUrl;
    private String contents;

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
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
        return this.number - rhs.number;
    }
}