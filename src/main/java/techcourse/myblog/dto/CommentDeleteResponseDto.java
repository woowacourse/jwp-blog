package techcourse.myblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDeleteResponseDto {
    Long commentId;

    public CommentDeleteResponseDto(Long commentId) {
        this.commentId = commentId;
    }
}
