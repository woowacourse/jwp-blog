package techcourse.myblog.domain;

public class Article {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;
    private long categoryId;

    public Article() {
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void update(Article articleParam) {
        this.title = articleParam.title;
        this.coverUrl = articleParam.coverUrl;
        this.contents = articleParam.contents;
        this.categoryId = articleParam.categoryId;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
