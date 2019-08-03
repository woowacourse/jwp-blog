package techcourse.myblog.dto;

import lombok.Getter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentDto {
    @NotBlank
    private String contents;

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public Comment toComment(User user, Article article) {
        return new Comment(contents, user, article);
    }
}
