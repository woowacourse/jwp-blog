package techcourse.myblog.service.dto;

public class CommentResponseDto {
    private String authorName;
    private String comment;

    public CommentResponseDto(String authorName, String comment) {
        this.authorName = authorName;
        this.comment = comment;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getComment() {
        return comment;
    }
}
