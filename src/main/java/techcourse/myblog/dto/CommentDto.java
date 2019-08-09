package techcourse.myblog.dto;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

public class CommentDto {
    private String contents;

    public CommentDto() {
    }

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Comment toEntity(Article article, User author) {
        return new Comment(contents, article, author);
    }
}
