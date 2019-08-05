package techcourse.myblog.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequestDto {
    private String contents;
    private Long articleId;
}
