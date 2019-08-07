package techcourse.myblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentSaveRequestDto {
    private String contents;
    private Long articleId;

    public CommentSaveRequestDto(String contents, Long articleId) {
        this.contents = contents;
        this.articleId = articleId;
    }
}
