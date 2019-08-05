package techcourse.myblog.service.dto;

import javax.validation.constraints.NotBlank;

public class CommentRequest {
    private long articleId;

    @NotBlank(message = "댓글을 입력하세요!")
    private String contents;

    public CommentRequest() {
    }

    public CommentRequest(Long articleId, String contents) {
        this.articleId = articleId;
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "CommentRequest{" +
                "contents='" + contents + '\'' +
                ", articleId=" + articleId +
                '}';
    }
}
