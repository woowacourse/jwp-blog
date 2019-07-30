package techcourse.myblog.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Configurable;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserDto;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String contents;
    private UserDto author;

    public CommentDto() {
    }

    @Builder
    public CommentDto(Long id, String contents, UserDto author) {
        this.id = id;
        this.contents = contents;
        this.author = author;
    }

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .contents(comment.getContents())
                .author(UserDto.from(comment.getAuthor()))
                .build();
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
                ", author=" + author +
                '}';
    }
}
