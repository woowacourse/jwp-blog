package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;
    private String contents;
    private long articleId;

    public Comment toComment(User user, Article article) {
        return new Comment(contents, user, article);
    }
}
