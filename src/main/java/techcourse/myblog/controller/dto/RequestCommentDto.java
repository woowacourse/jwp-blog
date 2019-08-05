package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestCommentDto {
    private Long articleId;
    private String contents;
}
