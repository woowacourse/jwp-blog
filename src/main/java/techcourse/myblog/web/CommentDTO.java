package techcourse.myblog.web;

import java.time.LocalDateTime;

public class CommentDTO {
    private long id;
    private String authorName;
    private String contents;
    private LocalDateTime createdTimeAt;

    public CommentDTO() {}

    public CommentDTO(long id, String authorName, String contents, LocalDateTime createdTimeAt) {
        this.id = id;
        this.authorName = authorName;
        this.contents = contents;
        this.createdTimeAt = createdTimeAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDateTime getCreatedTimeAt() {
        return this.createdTimeAt;
    }

    public void setCreatedTimeAt(LocalDateTime createdTimeAt) {
        this.createdTimeAt = createdTimeAt;
    }
}