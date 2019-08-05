package techcourse.myblog.service.dto;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

public class CommentRequestDto {
    private Long articleId;
    private String comment;

    private CommentRequestDto() {
    }

    public CommentRequestDto(Long articleId, String comment) {
        this.articleId = articleId;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Comment toEntity(User author, Article article) {
        return new Comment(comment, author, article);
    }
}
