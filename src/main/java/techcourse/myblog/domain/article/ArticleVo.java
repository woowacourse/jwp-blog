package techcourse.myblog.domain.article;

import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ArticleVo {
    private static final int CONTENTS_LENGTH = 1000;

    @Column(nullable = false)
    private String title;

    @URL
    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false, length = CONTENTS_LENGTH)
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
