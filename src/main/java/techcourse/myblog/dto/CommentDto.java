package techcourse.myblog.dto;

import lombok.Getter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentDto {
    @NotBlank
    private String contents;
    private User writer;
    private Article article;

    public CommentDto(String contents, User writer, Article article) {
        this.contents = contents;
        this.writer = writer;
        this.article = article;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Comment toComment() {
        return new Comment(contents, writer, article);
    }
}
