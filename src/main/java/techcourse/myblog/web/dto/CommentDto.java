package techcourse.myblog.web.dto;

import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

public class CommentDto {
    private User writer;
    private String contents;

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Comment create() {
        return new Comment(this.writer, this.contents);
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "writer=" + writer +
                ", contents='" + contents + '\'' +
                '}';
    }
}
