package techcourse.myblog.dto;

import lombok.Getter;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

@Getter
public class CommentResponse {
    private Long id;
    private String contents;
    private Long authorId;
    private String authorName;

    public CommentResponse(Comment comment, User author) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.authorId = author.getId();
        this.authorName = author.getName();
    }
}
