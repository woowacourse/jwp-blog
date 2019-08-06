package techcourse.myblog.web.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String message;
    private String time;

    public CommentResponse(final Long id, final String message) {
        this(id, message, LocalDateTime.now().toString());
    }

    public CommentResponse(final Long id, final String message, String time) {
        this.id = id;
        this.message = message;
        this.time = time;
    }
}
