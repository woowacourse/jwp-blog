package techcourse.myblog.domain;


public class Article {
    static final String DEFAULT_COVER_URL = "/images/default/bg.jpg";
    private String title;
    private String contents;
    private String coverUrl;

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        if (this.coverUrl.isEmpty()) {
            this.coverUrl = DEFAULT_COVER_URL;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        if (coverUrl.isEmpty()) {
            this.coverUrl = DEFAULT_COVER_URL;
            return;
        }
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}