package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.user.User;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private String contents;

    public Comment toEntity(User author) {
        return Comment.builder()
                .author(author)
                .contents(contents)
                .build();
    }
}
