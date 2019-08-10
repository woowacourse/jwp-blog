package techcourse.myblog.service.dto;

import javax.validation.constraints.NotNull;

public class CommentRequestDto {
    @NotNull
    private String contents;

    public CommentRequestDto() {
    }

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
