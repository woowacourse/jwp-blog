package techcourse.myblog.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateDto {
    @Length(max = 200)
    private String contents;
}
