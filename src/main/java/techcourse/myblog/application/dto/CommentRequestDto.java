package techcourse.myblog.application.dto;

import lombok.Getter;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentRequestDto {
    @NotBlank
    private String contents;

    private CommentRequestDto() {}

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }

    public Comment toComment(User writer, Article article) {
        return new Comment(contents, writer, article);
    }

    @Override
    public String toString() {
        return "CommentRequestDto{" +
                "contents=" + contents +
                '}';
    }
}
