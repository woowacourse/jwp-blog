package techcourse.myblog.service.comment;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.comment.CommentRequest;
import techcourse.myblog.service.dto.comment.CommentResponse;

public class CommentAssembler {
    public static Comment convertToEntity(CommentRequest commentRequest, User user, Article article) {
        return new Comment(commentRequest.getComment(), user, article);
    }

    public static CommentResponse convertToDto(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContents(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getArticle().getId(),
                comment.getCreatedDate());
    }
}
