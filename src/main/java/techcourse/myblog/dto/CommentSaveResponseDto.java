package techcourse.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveResponseDto {
    private String contents;
    private Long articleId;
    private String userName;
    private Long commentId;
}
