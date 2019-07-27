package techcourse.myblog.controller.dto;

import lombok.*;
import techcourse.myblog.model.User;

@Getter
@Setter
public class ArticleDto {
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
    private User author;
}
