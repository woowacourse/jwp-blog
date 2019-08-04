package techcourse.myblog.service.dto;

public class CommentRequestDto {
    private String contents;

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }
}
