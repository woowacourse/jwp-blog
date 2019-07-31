package techcourse.myblog.domain;

public class Article {
    private static long articleId = 1;

    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(String title, String coverUrl, String contents) {
        this(articleId++, title, coverUrl, contents);
    }

    public Article(long id, String title, String coverUrl, String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean equals(long articleId) {
        return this.id == articleId;
    }
}
