package techcourse.myblog.application;

import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.domain.comment.Comment;

public class CommentAssembler {
    public static CommentResponseDto buildCommentResponseDto(Comment comment) {
        return CommentResponseDto
                .builder()
                .commentId(comment.getId())
                .contents(comment.getContents())
                .userId(comment.getWriter().getId())
                .userName(comment.getWriter().getName())
                .updateTimeAt(comment.getUpdateTimeAt())
                .build();
    }
}
