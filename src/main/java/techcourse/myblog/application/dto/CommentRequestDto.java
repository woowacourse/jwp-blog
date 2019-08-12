package techcourse.myblog.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentRequestDto {
    @NotBlank
    private String contents;

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "CommentRequestDto{" +
                "contents=" + contents +
                '}';
    }
}
