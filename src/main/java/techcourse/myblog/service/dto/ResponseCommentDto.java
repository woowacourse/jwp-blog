package techcourse.myblog.service.dto;

import lombok.Getter;
import techcourse.myblog.domain.user.User;

@Getter
public class ResponseCommentDto {
    private String contents;
    private User author;

    public ResponseCommentDto(String contents, User author) {
        this.contents = contents;
        this.author = author;
    }
}
