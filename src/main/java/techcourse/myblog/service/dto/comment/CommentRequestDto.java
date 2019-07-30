package techcourse.myblog.service.dto.comment;

public class CommentRequestDto {
    private String comment;

    public CommentRequestDto(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
