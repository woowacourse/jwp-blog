package techcourse.myblog.domain;

import java.util.Objects;

public class ArticleDTO {
    private int id;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleDTO(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article convertToEntity() {
        return new Article(id, title, coverUrl, contents);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDTO that = (ArticleDTO) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(coverUrl, that.coverUrl) &&
                Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
