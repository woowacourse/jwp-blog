package techcourse.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.time.format.DateTimeFormatter;

public class CommentDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private Long articleId;
        private String contents;

        public Comment toComment(final Article article, final User user) {
            return Comment.builder()
                    .article(article)
                    .user(user)
                    .contents(contents)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        private Long id;
        private String contents;
        private String name;
        private String modifiedDate;

        Response(Comment comment) {
            this.id = comment.getId();
            this.contents = comment.getContents();
            this.modifiedDate = comment.getModifiedDate().format(DATE_TIME_FORMATTER);
            this.name = comment.getUser().getName();
        }

        public static Response createByComment(Comment comment) {
            return new Response(comment);
        }
    }
}
