package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseCommentDto {
    private Long id;
    private String userName;
    private LocalDateTime updatedTime;
    private String contents;
}
