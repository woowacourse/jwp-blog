package techcourse.myblog.dto;

import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class CommentDto {
    @NotBlank
    private String contents;

    public CommentDto() {
    }

    public CommentDto(@NotBlank String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
