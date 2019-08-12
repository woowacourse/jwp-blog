package techcourse.myblog.service.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentsResponse {
    private List<CommentResponse> comments;

    public CommentsResponse(List<CommentResponse> comments) {
        this.comments = new ArrayList<>(comments);
    }

    public List<CommentResponse> getComments() {
        return Collections.unmodifiableList(comments);
    }
}