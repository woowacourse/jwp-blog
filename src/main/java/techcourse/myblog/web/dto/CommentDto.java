package techcourse.myblog.web.dto;

import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

public class CommentDto {
    private User writer;
    private String contents;

    public CommentDto(User writer, String contents) {
        this.writer = writer;
        this.contents = contents;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
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
