package techcourse.myblog.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.User;

@Getter
@Setter
@ToString
public class RequestCommentDto {
    private Long articleId;
    private String contents;
    private User user;
    private Article article;
}
