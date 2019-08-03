package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDto {
    private Long articleId;
    private String contents;
}
