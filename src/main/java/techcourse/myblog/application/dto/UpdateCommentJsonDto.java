package techcourse.myblog.application.dto;

public class UpdateCommentJsonDto {
    private String email;
    private String contents;
    private Long articleId;
    private Long id;

    public UpdateCommentJsonDto(String email, String contents, Long articleId, Long id) {
        this.email = email;
        this.contents = contents;
        this.articleId = articleId;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
