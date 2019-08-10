package techcourse.myblog.application.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private UserResponse author;
    private String contents;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public CommentResponse(Long id, UserResponse author, String contents,
                           LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.id = id;
        this.author = author;
        this.contents = contents;
        this.createdTime = createdTime;
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

    public UserResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserResponse author) {
        this.author = author;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
