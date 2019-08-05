package techcourse.myblog.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ArticleVO {
    private String title;
    private String contents;
    private String coverUrl;

    @Builder
    public ArticleVO(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }
}
