package techcourse.myblog.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
