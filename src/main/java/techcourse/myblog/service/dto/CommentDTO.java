package techcourse.myblog.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

@Getter
@AllArgsConstructor
public class CommentDTO {
    private String contents;

    public Comment toDomain(Article article, User user) {
        return new Comment(contents, article, user);
    }
}
