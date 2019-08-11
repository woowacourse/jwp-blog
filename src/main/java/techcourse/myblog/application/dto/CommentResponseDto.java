package techcourse.myblog.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String contents;
    private String userName;
    private LocalDateTime updateTimeAt;
}
