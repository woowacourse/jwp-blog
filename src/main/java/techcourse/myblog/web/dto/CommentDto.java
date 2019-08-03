package techcourse.myblog.web.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

public class CommentDto {
    private String contents;

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public Comment create(User author, Article article) {
        return new Comment(author, contents, article);
    }
}
