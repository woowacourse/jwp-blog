package techcourse.myblog.domain.vo;

import javax.persistence.Embeddable;

@Embeddable
public class CommentContents {
    private String contents;

    private CommentContents() {
    }

    public CommentContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }
}
