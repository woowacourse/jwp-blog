package techcourse.myblog.comment.dto;

import lombok.Data;
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
}
