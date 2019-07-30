package techcourse.myblog.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long articleId;
    private String contents;
}
