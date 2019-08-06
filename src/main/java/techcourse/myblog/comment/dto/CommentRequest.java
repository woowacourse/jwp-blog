package techcourse.myblog.comment.dto;

public class CommentRequest {
    private String comment;

    public CommentRequest() {
    }

    public CommentRequest(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
