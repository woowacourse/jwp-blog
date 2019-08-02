package techcourse.myblog.dto;

public class CommentResponse {
    private long id;
    private String contents;
    private String author;
    private String createdDate;

    public CommentResponse(long id, String contents, String author, String createdDate) {
        this.id = id;
        this.contents = contents;
        this.author = author;
        this.createdDate = createdDate;
    }

    public long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
