package techcourse.myblog.dto;

import lombok.Getter;

@Getter
public class CommentResponse {
    private Long id;
    private String contents;
    private Long authorId;
    private String authorName;

    public CommentResponse(Long id, String contents, Long authorId, String authorName) {
        this.id = id;
        this.contents = contents;
        this.authorId = authorId;
        this.authorName = authorName;
    }
}
