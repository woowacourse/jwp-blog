package techcourse.myblog.dto;

import techcourse.myblog.domain.comment.Comment;

public class CommentDto {
    private String contents;

    public CommentDto(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public Comment toEntity() {
        return new Comment(contents);
    }
}
