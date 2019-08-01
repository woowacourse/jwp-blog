package techcourse.myblog.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Configurable;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.domain.user.UserInfoDto;

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
                .author(UserInfoDto.from(comment.getAuthor()))
                .build();
    }

    public Comment toEntity(UserDto userDto, ArticleDto articleDto) {
        return Comment.builder().article(articleDto.toEntity()).author(userDto.toEntity()).contents(this.contents).build();
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
