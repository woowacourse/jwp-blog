package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {
    private String comment;

    public Comment toComment(User commenter, Article article) {
        return Comment.builder()
                .comment(this.comment)
                .article(article)
                .commenter(commenter)
                .build();
    }

    public Comment toComment() {
        return Comment.builder()
                .comment(this.comment)
                .build();
    }
}

