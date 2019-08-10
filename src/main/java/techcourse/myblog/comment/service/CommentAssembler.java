package techcourse.myblog.comment.service;

import techcourse.myblog.article.Article;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.dto.CommentRequest;
import techcourse.myblog.comment.dto.CommentResponse;
import techcourse.myblog.user.User;

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
