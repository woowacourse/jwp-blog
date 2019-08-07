package techcourse.myblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentSaveResponseDto {
    private String contents;
    private Long articleId;
    private String userName;
    private Long commentId;

    public CommentSaveResponseDto(String contents, Long articleId, String userName, Long commentId) {
        this.contents = contents;
        this.articleId = articleId;
        this.userName = userName;
        this.commentId = commentId;
    }
}
