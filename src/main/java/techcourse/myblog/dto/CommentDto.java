package techcourse.myblog.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDto {
    @NotBlank(message = "댓글을 입력하세요")
    private String contents;

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public Comment toComment(User user, Article article) {
        return new Comment(contents, user, article);
    }
}
