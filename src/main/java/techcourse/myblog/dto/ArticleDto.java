package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDto {
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
}
