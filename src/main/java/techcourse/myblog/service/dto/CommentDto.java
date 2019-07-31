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
    private User writer;
    private Article article;

    public CommentDto(String contents, User writer, Article article) {
        this.contents = contents;
        this.writer = writer;
        this.article = article;
    }

    public Comment toComment() {
        return new Comment(contents, writer, article);
    }
}
