package techcourse.myblog.application.dto;

public class CommentResponse {
    private Long id;
    private String contents;
    private UserResponse author;

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
}
