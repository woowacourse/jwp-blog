package techcourse.myblog.controller.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.User;

@Getter
@Setter
public class CommentDto {
    private Long articleId;
    private String contents;
    private User user;
    private Article article;
}
