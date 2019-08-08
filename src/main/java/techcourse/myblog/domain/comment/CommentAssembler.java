package techcourse.myblog.domain.comment;

import techcourse.myblog.service.dto.ResponseCommentDto;

public class CommentAssembler {

    public static ResponseCommentDto toResponseDto(Comment comment) {
        return new ResponseCommentDto(comment.getContents(), comment.getAuthor());
    }

//    public static Comment toEntity(CommentDto commentDto, User author) {

//    }
}
