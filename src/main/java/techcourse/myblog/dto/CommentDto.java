package techcourse.myblog.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class CommentDto {
    @Data
    public static class Create {
        private Long articleId;

        @Size(max = 300)
        @NotBlank
        private String contents;

        private LocalDateTime regDate;

        public Comment toComment(User user, Article article) {
            return Comment.builder()
                .user(user)
                .article(article)
                .regDate(regDate)
                .contents(contents)
                .build();
        }
    }

    @Data
    public static class Update {
        private Long articleId;

        @Size(max = 300)
        @NotBlank
        private String contents;
        private LocalDateTime regDate;
        private LocalDateTime modifiedDate;

        public Comment toComment(Long id, User user, Article article) {
            return Comment.builder()
                .id(id)
                .user(user)
                .article(article)
                .regDate(regDate)
                .modfiedDate(modifiedDate)
                .contents(contents)
                .build();
        }
    }

    @Data
    @EqualsAndHashCode(exclude = {"regDate", "modifiedDate"})
    public static class Response {
        private Long articleId;
        private LocalDateTime regDate;
        private LocalDateTime modifiedDate;
        private String contents;
    }
}
