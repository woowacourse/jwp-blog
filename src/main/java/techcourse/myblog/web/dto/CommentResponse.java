package techcourse.myblog.web.dto;

import techcourse.myblog.domain.Comment;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long id;
    private String contents;
    private String authorName;
    private LocalDateTime createdTime;

    public CommentResponse() {
    }

    public CommentResponse(Long id, String contents, String authorName, LocalDateTime createdTime) {
        this.id = id;
        this.contents = contents;
        this.authorName = authorName;
        this.createdTime = createdTime;
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getContents(),
            comment.getAuthor().getName(),
            comment.getCreatedTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }


}
