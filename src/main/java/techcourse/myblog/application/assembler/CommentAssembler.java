package techcourse.myblog.application.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

@Component
public class CommentAssembler {
    @Autowired
    private UserAssembler userAssembler;

    public CommentResponse convertToResponse(Comment comment, User author) {
        return new CommentResponse(comment.getId(), userAssembler.convertToUserResponse(author),
                comment.getContents(), comment.getCreatedTime(), comment.getUpdatedTime());
    }
}
