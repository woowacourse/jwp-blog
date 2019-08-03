package techcourse.myblog.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

public class ArticleRequest {
    @NotNull
    private String title;
    @Lob
    @NotNull
    private String contents;
    private String coverUrl;

    public ArticleRequest() {
    }

    public ArticleRequest(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
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
        this.coverUrl = coverUrl;
    }
}