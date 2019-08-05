package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class CommentVO {
    String comment;

    public CommentVO(String comment) {
        this.comment = comment;
    }
}
