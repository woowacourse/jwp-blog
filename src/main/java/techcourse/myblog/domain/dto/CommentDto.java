package techcourse.myblog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String userName;
    private String contents;

    public Comment toEntity(User author) {
        return Comment.builder()
                .author(author)
                .contents(contents)
                .build();
    }
}
