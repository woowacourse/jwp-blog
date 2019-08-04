package techcourse.myblog.web.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.CommentRequestDto;

public class CommentAssembler {
    public static Comment convertToEntity(CommentRequestDto commentRequestDto, User user, Article article) {
        return new Comment(commentRequestDto.getContents(), user, article);
    }

    public static CommentDto convertToDto(Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getContents(),
            comment.getAuthor().getId(),
            comment.getAuthor().getName());
    }
}
