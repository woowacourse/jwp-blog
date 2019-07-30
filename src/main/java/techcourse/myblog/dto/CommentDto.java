package techcourse.myblog.dto;

import lombok.Data;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.time.LocalDateTime;

public class CommentDto {
    @Data
    public static class Create {
        private Long articleId;
        private String contents;

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
        private Long id;
        private Long articleId;
        private String contents;

        public Comment toComment() {
            return Comment.builder()
                .contents(contents)
                .build();
        }
    }

    @Data
    public static class Response {
        private Long id;
        private Long articleId;
        private LocalDateTime regDate;
        private String contents;
    }
}
