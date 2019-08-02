package techcourse.myblog.dto;

public class CommentRequest {
    private String contents;

    public CommentRequest(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }
}
