package techcourse.myblog.controller.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

@Data
@Slf4j
public class CommentDto {
    private long id;
    private String contents;
    private long articleId;

    public Comment toComment(User user, Article article) {
        if (id != 0) {
            return new Comment(id, contents, user, article);
        }
        return new Comment(contents, user, article);
    }
}
