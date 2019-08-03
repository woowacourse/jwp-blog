package techcourse.myblog.controller.dto;

import lombok.*;

@AllArgsConstructor
@Getter
public class ArticleDto {
    private String title;
    private String coverUrl;
    private String contents;
}
