package techcourse.myblog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @Setter
    @NoArgsConstructor
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
    public static class Response {
        private Long id;
        private String contents;
        private String name;
        @JsonFormat(pattern = "yyyy-mm-dd HH:mm")
        private LocalDateTime modifiedDate;

        Response(Comment comment) {
            this.id = comment.getId();
            this.contents = comment.getContents();
            this.modifiedDate = comment.getModifiedDate();
            this.name = comment.getUser().getName();
        }

        public static Response createByComment(Comment comment) {
            return new Response(comment);
        }
    }
}
