package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.article.Article;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.user.User;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private String contents;

    public Comment toEntity(Article article, User author) {
        return Comment.builder()
                .article(article)
                .author(author)
                .contents(contents)
                .build();
    }
}
