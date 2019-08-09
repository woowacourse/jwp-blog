package techcourse.myblog.domain;

import java.util.Collections;
import java.util.List;

public class Comments {
    private final List<Comment> comments;

    public Comments(List<Comment> comments) {
        this.comments = Collections.unmodifiableList(comments);
    }

    public List<Comment> getComments() {
        return comments;
    }
}
