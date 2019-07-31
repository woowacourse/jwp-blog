package techcourse.myblog.comment.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CommentUpdateDto {
    @Length(max = 200)
    private String contents;
}
