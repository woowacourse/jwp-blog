package techcourse.myblog.domain;

public class ArticleDto {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;
    private long categoryId;

    public ArticleDto() {
    }

    public ArticleDto(long id, String title, String coverUrl, String contents, long categoryId) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.categoryId = categoryId;
    }

    public static ArticleDto from(Article article) {
        return new ArticleDto(article.getId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents(),
                article.getCategoryId());
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

    @Override
    public String toString() {
        return "ArticleDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
