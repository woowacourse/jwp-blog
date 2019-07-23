package techcourse.myblog.dto;

import lombok.Getter;

@Getter
public class ArticleDto {
    private final String title;
    private final String coverUrl;
    private final String contents;

    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }
}
