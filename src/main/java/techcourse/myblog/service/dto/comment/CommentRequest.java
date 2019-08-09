package techcourse.myblog.service.dto.comment;

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
