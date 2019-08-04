package techcourse.myblog.domain;

import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;

public class CommentAssembler {
    public static Comment toEntity(CommentRequest dto) {
        String contents = dto.getContents();

        return new Comment(contents);
    }

    public static CommentResponse toDto(Comment comment) {
        Long id = comment.getId();
        String contents = comment.getContents();
        User commenter = comment.getCommenter();

        return new CommentResponse(id, contents, new User(commenter.getName(), commenter.getEmail()));
    }
}
