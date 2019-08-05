package techcourse.myblog.comment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.user.domain.User;

@Getter
@Setter
public class CommentResponseDto {
    private long id;
    private String contents;
    private User author;

    @JsonIgnore
    private Article article;
}
