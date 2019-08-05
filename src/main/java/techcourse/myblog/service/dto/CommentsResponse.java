package techcourse.myblog.service.dto;

import java.util.List;

public class CommentsResponse {
    private List<CommentResponse> comments;

    public CommentsResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }
}