package techcourse.myblog.comment.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.user.domain.User;

@Getter
@Setter
public class CommentCreateDto {
    private String contents;

    public Comment toComment(User author, Article article) {
        return Comment.builder()
                .contents(contents)
                .author(author)
                .article(article)
                .build();
    }
}
