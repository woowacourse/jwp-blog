package techcourse.myblog.service.comment;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.coment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.comment.CommentRequestDto;
import techcourse.myblog.service.dto.comment.CommentResponseDto;

public class CommentAssembler {
    public static Comment convertToEntity(CommentRequestDto commentRequestDto, User user, Article article) {
        return new Comment(commentRequestDto.getComment(), user, article);
    }

    public static CommentResponseDto convertToDto(Comment comment) {
        return new CommentResponseDto(comment.getContents(), comment.getAuthor().getName());
    }
}
