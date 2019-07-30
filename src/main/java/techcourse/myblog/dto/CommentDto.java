package techcourse.myblog.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.time.LocalDateTime;

public class CommentDto {
    //TODO : articleId를 가져도 되는지 ? 엔티티에는 없지만
    @Data
    public static class Create {
        private Long articleId;
        private String contents;
        private LocalDateTime regDate;

        //최초 생성 id필요없음, 수정 날짜 필요없음
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
