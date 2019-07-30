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
        return Comment.builder().article(article).author(user).contents(this.contents).build();

    }

    public Comment toEntity() {
        return Comment.builder().contents(contents).build();
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                '}';
    }
}
