package techcourse.myblog.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ArticleResponseDto {
    private Long articleId;
    private Long userId;
    private String title;
    private String coverUrl;
    private String contents;
    private String email;
    private String name;
    private LocalDateTime updateTimeAt;
}
