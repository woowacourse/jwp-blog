package techcourse.myblog.web.view;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private final Long id;
    private final LocalDateTime time;

    public CommentResponse(final Long id) {
        this.id = id;
        time = LocalDateTime.now();
    }


}
