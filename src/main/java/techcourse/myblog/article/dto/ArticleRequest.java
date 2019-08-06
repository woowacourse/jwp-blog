package techcourse.myblog.article.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ArticleRequest {
    @NotNull
    private String title;

    @NotNull
    private String coverUrl;

    @NotNull
    private String contents;

    public ArticleRequest(@NotNull String title, @NotNull String coverUrl, @NotNull String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
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
        ArticleRequest that = (ArticleRequest) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(coverUrl, that.coverUrl) &&
                Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, coverUrl, contents);
    }
}
