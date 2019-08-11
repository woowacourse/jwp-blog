package techcourse.myblog.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentRequestDto {
    @NotBlank
    private String contents;

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "CommentRequestDto{" +
                "contents=" + contents +
                '}';
    }
}
