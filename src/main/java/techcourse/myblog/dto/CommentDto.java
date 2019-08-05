package techcourse.myblog.dto;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

public class CommentDto {
    private String contents;

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public Comment toEntity(Article article, User author) {
        return new Comment(contents, article, author);
    }

    public static class JSON {
        private String contents;

        public JSON() {
        }

        public JSON(String contents) {
            this.contents = contents;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public CommentDto toDto() {
            return new CommentDto(this.contents);
        }
    }
}
