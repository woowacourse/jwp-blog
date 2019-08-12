package techcourse.myblog.application;

import techcourse.myblog.application.dto.CommentRequestDto;
import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

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

    public static Comment toEntity(CommentRequestDto commentRequestDto, User writer, Article article) {
        return new Comment(commentRequestDto.getContents(), writer, article);
    }
}
