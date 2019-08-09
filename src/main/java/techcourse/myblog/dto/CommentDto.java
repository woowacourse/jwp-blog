package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import techcourse.myblog.domain.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class CommentDto {
    @NotBlank
    private long id;

    @NotBlank
    private String contents;

    @NotBlank
    private LocalDateTime updatedDate;

    @NotBlank
    private User author;

    public CommentDto() {
    }

    public CommentDto(@NotBlank long id, @NotBlank String contents, @NotBlank LocalDateTime updatedDate, @NotBlank User author) {
        this.id = id;
        this.contents = contents;
        this.updatedDate = updatedDate;
        this.author = author;
    }
}
