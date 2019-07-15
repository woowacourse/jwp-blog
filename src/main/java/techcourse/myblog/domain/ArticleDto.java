package techcourse.myblog.domain;

import java.util.Objects;

public class ArticleDto {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleDto() {

    }

    public ArticleDto(long id, String title, String coverUrl, String contents) {
        this(title, coverUrl, contents);
        this.id = id;
    }

    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
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

    public Article toArticle(final long id) {
        return new Article(id, title, coverUrl, contents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleDto)) return false;
        ArticleDto that = (ArticleDto) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(coverUrl, that.coverUrl) &&
                Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, coverUrl, contents);
    }

    @Override
    public String toString() {
        return "ArticleDto{" +
                "title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
