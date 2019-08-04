package techcourse.myblog.dto.comment;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

public class CommentRequest {
    @Lob
    @NotNull
    private String contents;

    public CommentRequest() {
    }

    public CommentRequest(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
