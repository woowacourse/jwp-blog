package techcourse.myblog.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private long id;
    private String comment;
    private User commenter;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.commenter = comment.getCommenter();
        this.createdAt = comment.getCreatedAt();
    }
}
