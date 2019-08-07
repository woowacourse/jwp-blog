package techcourse.myblog.service.dto.response;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;

public class CommentResponseDto {
    private String userName;
    private String contents;
    private Long id;

    public CommentResponseDto(Article article, Comment comment) {
        this.userName = article.getAuthor().getUserName();
        this.contents = comment.getContents();
        this.id = comment.getId();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
