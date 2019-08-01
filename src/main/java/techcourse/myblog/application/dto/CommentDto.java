package techcourse.myblog.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private long id;
    private String contents;
    private String userName;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
