package techcourse.myblog.service.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

public class CommentDto {
    private String contents;

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public CommentDto() {
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Comment toDomain(Article article, User user) {
        return new Comment(contents, article, user);
    }
}
