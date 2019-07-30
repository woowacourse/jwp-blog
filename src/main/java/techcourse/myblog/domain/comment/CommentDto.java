package techcourse.myblog.domain.comment;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String contents;

    public CommentDto() {
    }

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public Comment toEntity(User user, Article article) {
        return Comment.builder().article(article).author(user).build();

    }

    public Comment toEntity() {
        return Comment.builder().contents(contents).build();
    }
}
