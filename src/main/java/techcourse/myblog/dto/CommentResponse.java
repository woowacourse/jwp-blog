package techcourse.myblog.dto;

import lombok.Getter;
import techcourse.myblog.domain.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long id;
    private String contents;
    private LocalDateTime updateTimeAt;
    private UserResponse writer;

    private CommentResponse(Long id, String contents, LocalDateTime updateTimeAt, UserResponse writer) {
        this.id = id;
        this.contents = contents;
        this.updateTimeAt = updateTimeAt;
        this.writer = writer;
    }

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getId(),
                comment.getContents(),
                comment.getUpdateTimeAt(),
                UserResponse.of(comment.getWriter())
        );
    }
}
