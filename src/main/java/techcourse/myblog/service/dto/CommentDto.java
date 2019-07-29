package techcourse.myblog.service.dto;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

public class CommentDto {
    private String comment;

    public CommentDto(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public Comment toEntity(User author, Article article) {
        return new Comment(comment, author, article);
    }
}
