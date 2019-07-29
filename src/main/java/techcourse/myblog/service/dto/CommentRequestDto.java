package techcourse.myblog.service.dto;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

public class CommentRequestDto {
    private Long articleId;
    private String comment;

    public CommentRequestDto(Long articleId, String comment) {
        this.articleId = articleId;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Comment toEntity(User author, Article article) {
        return new Comment(comment, author, article);
    }
}
