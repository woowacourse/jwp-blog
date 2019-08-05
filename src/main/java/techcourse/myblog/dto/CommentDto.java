package techcourse.myblog.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

@Data
@Slf4j
public class CommentDto {
    private String contents;
    private long articleId;

    public Comment toComment(User user, Article article) {
        return new Comment(contents, user, article);
    }
}
