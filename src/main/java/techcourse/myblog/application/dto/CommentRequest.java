package techcourse.myblog.application.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import javax.validation.constraints.NotBlank;

public class CommentRequest {

    @NotBlank(message = "댓글을 입력해주세요!")
    private String contents;

    public CommentRequest() {
    }

    public CommentRequest(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Comment toEntity(User author, Article article) {
        return new Comment(contents, author, article);
    }
}
