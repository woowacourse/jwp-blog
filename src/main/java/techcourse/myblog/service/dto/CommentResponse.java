package techcourse.myblog.service.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String contents;
    private LocalDateTime createdDate;
    private UserResponse commenter;

    public CommentResponse(Long id, String contents, LocalDateTime createdDate, UserResponse commenter) {
        this.id = id;
        this.contents = contents;
        this.createdDate = createdDate;
        this.commenter = commenter;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public UserResponse getCommenter() {
        return commenter;
    }

    public void setCommenter(UserResponse commenter) {
        this.commenter = commenter;
    }
}
