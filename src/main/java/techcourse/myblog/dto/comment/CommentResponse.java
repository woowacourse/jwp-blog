package techcourse.myblog.dto.comment;

import techcourse.myblog.domain.user.User;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String contents;
    private User commenter;
    private LocalDateTime updatedTime;

    public CommentResponse() {
    }

    public CommentResponse(Long id, String contents, User commenter, LocalDateTime updatedTime) {
        this.id = id;
        this.contents = contents;
        this.commenter = commenter;
        this.updatedTime = updatedTime;
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

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
