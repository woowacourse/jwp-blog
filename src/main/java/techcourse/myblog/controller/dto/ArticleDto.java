package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArticleDto {
    private String title;
    private String coverUrl;
    private String contents;
}
