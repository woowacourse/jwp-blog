package techcourse.myblog.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseCommentDto {
    private Long commentId;
    private String contents;
    private String userName;
    private LocalDateTime createdTime;

    public ResponseCommentDto(Long commentId, String contents, String userName, LocalDateTime createdTime) {
        this.commentId = commentId;
        this.contents = contents;
        this.userName = userName;
        this.createdTime = createdTime;
    }
}
