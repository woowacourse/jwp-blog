package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDto {
    private String contents;

    public CommentDto(String contents) {
        this.contents = contents;
    }
}
