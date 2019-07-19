package techcourse.myblog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }
}
