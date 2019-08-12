package techcourse.myblog.service.dto;

import lombok.Getter;
import techcourse.myblog.domain.user.User;

@Getter
public class ResponseCommentDto {
    private long id;
    private String contents;
    private User author;

    public ResponseCommentDto(long id,String contents, User author) {
        this.id = id;
        this.contents = contents;
        this.author = author;
    }
}
