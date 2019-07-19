package techcourse.myblog.domain;

import lombok.Getter;

@Getter
public class ArticleVo {
    private final String title;
    private final String coverUrl;
    private final String contents;

    public ArticleVo(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }
}
