package techcourse.myblog.domain.comment;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.comment.CommentRequestDto;
import techcourse.myblog.service.comment.CommentResponseDto;

public class CommentAssembler {
    public static Comment convertToEntity(CommentRequestDto commentRequestDto, User user, Article article) {
        return new Comment(commentRequestDto.getContents(), user, article);
    }

    public static CommentResponseDto convertToDto(Comment comment) {
        return new CommentResponseDto(
            comment.getId(),
            comment.getContents(),
            comment.getAuthor().getId(),
            comment.getAuthor().getName());
    }
}
