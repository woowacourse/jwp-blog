package techcourse.myblog.domain.article;

import javax.persistence.Embeddable;

@Embeddable
public class ArticleVo {
    private String title;
    private String coverUrl;
    private String contents;

    private ArticleVo() {
    }

    public ArticleVo(String title, String coverUrl, String contents) {
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
}
