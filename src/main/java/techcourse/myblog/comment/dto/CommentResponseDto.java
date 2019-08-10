package techcourse.myblog.comment.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.user.domain.User;

@Getter
@Setter
public class CommentResponseDto {
    private long id;
    private String contents;
    private User author;
}
