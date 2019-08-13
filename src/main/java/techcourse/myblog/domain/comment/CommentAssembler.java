package techcourse.myblog.domain.comment;

import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.comment.CommentRequest;
import techcourse.myblog.dto.comment.CommentResponse;

import java.time.LocalDateTime;

public class CommentAssembler {
    public static Comment toEntity(CommentRequest dto) {
        String contents = dto.getContents();

        return new Comment(contents);
    }

    public static CommentResponse toDto(Comment comment) {
        Long id = comment.getId();
        String contents = comment.getContents();
        User commenter = comment.getCommenter();
        LocalDateTime updatedTime = comment.getUpdatedTime();

        return new CommentResponse(id, contents, new User(commenter.getId(), commenter.getName(), commenter.getEmail()), updatedTime);
    }
}
