package techcourse.myblog.application.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class CommentDto {
    private long id;
    private String contents;
    private String userName;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;

    public static CommentDto of(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContents(comment.getContents());
        commentDto.setUserName(comment.getUser().getName());
        commentDto.setCreateDateTime(comment.getCreateDateTime());
        commentDto.setUpdateDateTime(comment.getCreateDateTime());

        return commentDto;
    }
}