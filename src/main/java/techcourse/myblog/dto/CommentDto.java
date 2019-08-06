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

        public Create(Long articleId, String contents) {
            this.articleId = articleId;
            this.contents = contents;
        }

        public Comment toComment(User user, Article article) {
            return Comment.builder()
                .user(user)
                .article(article)
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

        public Update(Long articleId, String contents) {
            this.articleId = articleId;
            this.contents = contents;
        }
    }

    @Data
    @EqualsAndHashCode(of = "id")
    public static class Response {
        private Long id;
        private Long articleId;
        private LocalDateTime regDate;
        private LocalDateTime modifiedDate;
        private String contents;
        private User user;
    }
}
