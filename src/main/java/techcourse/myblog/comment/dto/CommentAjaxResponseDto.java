package techcourse.myblog.comment.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.comment.domain.Comment;

@Setter
@Getter
public class CommentAjaxResponseDto {
    private long id;
    private String contents;
    private long userId;
    private String userName;

    public CommentAjaxResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.userId = comment.getUserId();
        this.userName = comment.getUserName();
    }
}
