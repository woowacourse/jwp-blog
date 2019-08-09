package techcourse.myblog.application.dto;

import lombok.*;
import techcourse.myblog.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class CommentResponseDto {
    private long id;
    private String contents;
    private String userName;
    private String userEmail;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
    private long articleId;

    public static CommentResponseDto of(Comment comment) {
        return builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .userName(comment.getUser().getName())
                .userEmail(comment.getUser().getEmail())
                .createDateTime(comment.getCreateDateTime())
                .updateDateTime(comment.getUpdateDateTime())
                .articleId(comment.getArticle().getId())
                .build();
    }
}