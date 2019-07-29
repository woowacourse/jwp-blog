package techcourse.myblog.comment.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.user.domain.User;

public class CommentDto {

    @Data
    public static class Creation {
        private String contents;

        public Comment toComment(User author, Article article) {
            return Comment.builder()
                    .contents(contents)
                    .author(author)
                    .article(article)
                    .build();
        }
    }

    @Data
    public static class Updation {
        @Length(max = 200)
        private String contents;
    }

    @Data
    public static class Response {
        private long id;
        private String contents;
        private User author;
    }
}
