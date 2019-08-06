package techcourse.myblog.dto;

import lombok.Getter;

@Getter
public class CommentRequest {
    private Long articleId;
    private String contents;

    public CommentRequest() {
    }

    public CommentRequest(Long articleId, String contents) {
        this.articleId = articleId;
        this.contents = contents;
    }
}
