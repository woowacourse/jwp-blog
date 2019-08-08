package techcourse.myblog.domain.comment;

import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.service.dto.ResponseCommentDto;

public class CommentAssembler {

    public static Comment toEntity(CommentDto commentDto, User author) {
        return Comment.builder()
                .author(author)
                .contents(commentDto.getContents())
                .build();
    }

    public static ResponseCommentDto toResponseDto(Comment comment) {
        return new ResponseCommentDto(comment.getId(), comment.getContents(), comment.getAuthor());
    }
}
