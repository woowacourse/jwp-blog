package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArticleDto {
    private String title;
    private String contents;
    private String coverUrl;
}
