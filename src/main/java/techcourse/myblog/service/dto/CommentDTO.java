package techcourse.myblog.service.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

public class CommentDTO {
    public CommentDTO(String contents) {
        this.contents = contents;
    }

    private String contents;

    public String getContents() {
        return contents;
    }

    public Comment toDomain(Article article, User user) {
        return new Comment(contents, article, user);
    }
}
