package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.Comment;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    private long id;
    private String comment;
    private String commenter;
    private long minAgo;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.commenter = comment.getCommenter().getName();
        this.minAgo = Duration.between(comment.getCreatedAt(), LocalDateTime.now()).toMinutes();
    }
}
