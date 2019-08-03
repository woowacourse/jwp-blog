package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentDto {
    @NotBlank
    private String contents;

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public Comment toComment(User writer, Article article) {
        return new Comment(contents, writer, article);
    }
}
